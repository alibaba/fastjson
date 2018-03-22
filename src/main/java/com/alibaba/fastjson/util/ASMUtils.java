package com.alibaba.fastjson.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.asm.ClassReader;
import com.alibaba.fastjson.asm.TypeCollector;
import com.alibaba.fastjson.parser.ParserConfig;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ASMUtils {

    public static final String JAVA_VM_NAME = System.getProperty("java.vm.name");
    
    public static final boolean IS_ANDROID = isAndroid(JAVA_VM_NAME);
	
    public static boolean isAndroid(String vmName) {
        if (vmName == null) { // default is false
            return false;
        }
        
        String lowerVMName = vmName.toLowerCase();
        
        return lowerVMName.contains("dalvik") //
               || lowerVMName.contains("lemur") // aliyun-vm name
        ;
    }

    public static String desc(Method method) {   
    	Class<?>[] types = method.getParameterTypes();
        StringBuilder buf = new StringBuilder((types.length + 1) << 4);
        buf.append('(');
        for (int i = 0; i < types.length; ++i) {
            buf.append(desc(types[i]));
        }
        buf.append(')');
        buf.append(desc(method.getReturnType()));
        return buf.toString();
    }

    public static String desc(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return getPrimitiveLetter(returnType);
        } else if (returnType.isArray()) {
            return "[" + desc(returnType.getComponentType());
        } else {
            return "L" + type(returnType) + ";";
        }
    }

    public static String type(Class<?> parameterType) {
        if (parameterType.isArray()) {
            return "[" + desc(parameterType.getComponentType());
        } else {
            if (!parameterType.isPrimitive()) {
                String clsName = parameterType.getName();
                return clsName.replace('.', '/'); // 直接基于字符串替换，不使用正则替换
            } else {
                return getPrimitiveLetter(parameterType);
            }
        }
    }
    

    public static String getPrimitiveLetter(Class<?> type) {
        if (Integer.TYPE == type) {
            return "I";
        } else if (Void.TYPE == type) {
            return "V";
        } else if (Boolean.TYPE == type) {
            return "Z";
        } else if (Character.TYPE == type) {
            return "C";
        } else if (Byte.TYPE == type) {
            return "B";
        } else if (Short.TYPE == type) {
            return "S";
        } else if (Float.TYPE == type) {
            return "F";
        } else if (Long.TYPE == type) {
            return "J";
        } else if (Double.TYPE == type) {
            return "D";
        }

        throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
    }

    public static Type getMethodType(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);

            return method.getGenericReturnType();
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean checkName(String name) {
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (c < '\001' || c > '\177' || c == '.') {
                return false;
            }
        }
        
        return true;
    }


    public static String[] lookupParameterNames(AccessibleObject methodOrCtor) {
        if (IS_ANDROID) {
            return new String[0];
        }

        final Class<?>[] types;
        final Class<?> declaringClass;
        final String name;

        Annotation[][] parameterAnnotations;
        if (methodOrCtor instanceof Method) {
            Method method = (Method) methodOrCtor;
            types = method.getParameterTypes();
            name = method.getName();
            declaringClass = method.getDeclaringClass();
            parameterAnnotations = method.getParameterAnnotations();
        } else {
            Constructor<?> constructor = (Constructor<?>) methodOrCtor;
            types = constructor.getParameterTypes();
            declaringClass = constructor.getDeclaringClass();
            name = "<init>";
            parameterAnnotations = constructor.getParameterAnnotations();
        }

        if (types.length == 0) {
            return new String[0];
        }

        ClassLoader classLoader = declaringClass.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        String className = declaringClass.getName();
        String resourceName = className.replace('.', '/') + ".class";
        InputStream is = classLoader.getResourceAsStream(resourceName);

        if (is == null) {
            return new String[0];
        }

        try {
            ClassReader reader = new ClassReader(is, false);
            TypeCollector visitor = new TypeCollector(name, types);
            reader.accept(visitor);
            String[] parameterNames = visitor.getParameterNamesForMethod();

            for (int i = 0; i < parameterNames.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                if (annotations != null) {
                    for (int j = 0; j < annotations.length; j++) {
                        if (annotations[j] instanceof JSONField) {
                            JSONField jsonField = (JSONField) annotations[j];
                            String fieldName = jsonField.name();
                            if (fieldName != null && fieldName.length() > 0) {
                                parameterNames[i] = fieldName;
                            }
                        }
                    }
                }
            }

            return parameterNames;
        } catch (IOException e) {
            return new String[0];
        } finally {
            IOUtils.close(is);
        }
    }
}
