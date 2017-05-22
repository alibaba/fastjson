package com.alibaba.fastjson.util;

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
}
