package com.alibaba.fastjson.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

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

    public static String getDesc(Method method) {   
    	Class<?>[] types = method.getParameterTypes();
        StringBuilder buf = new StringBuilder((types.length + 1) << 4);
        buf.append('(');
        for (int i = 0; i < types.length; ++i) {
            buf.append(getDesc(types[i]));
        }
        buf.append(')');
        buf.append(getDesc(method.getReturnType()));
        return buf.toString();
    }

    public static String getDesc(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return getPrimitiveLetter(returnType);
        } else if (returnType.isArray()) {
            return "[" + getDesc(returnType.getComponentType());
        } else {
            return "L" + getType(returnType) + ";";
        }
    }

    public static String getType(Class<?> parameterType) {
        if (parameterType.isArray()) {
            return "[" + getDesc(parameterType.getComponentType());
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
        if (Integer.TYPE.equals(type)) {
            return "I";
        } else if (Void.TYPE.equals(type)) {
            return "V";
        } else if (Boolean.TYPE.equals(type)) {
            return "Z";
        } else if (Character.TYPE.equals(type)) {
            return "C";
        } else if (Byte.TYPE.equals(type)) {
            return "B";
        } else if (Short.TYPE.equals(type)) {
            return "S";
        } else if (Float.TYPE.equals(type)) {
            return "F";
        } else if (Long.TYPE.equals(type)) {
            return "J";
        } else if (Double.TYPE.equals(type)) {
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

    public static Type getFieldType(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);

            return field.getGenericType();
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void parseArray(Collection collection, //
                                  ObjectDeserializer deser, //
                                  DefaultJSONParser parser, //
                                  Type type, //
                                  Object fieldName) {

        final JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
        }

        parser.accept(JSONToken.LBRACKET, JSONToken.LBRACKET);

        int index = 0;
        for (;;) {
            Object item = deser.deserialze(parser, type, index);
            collection.add(item);
            index++;
            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LBRACKET);
            } else {
                break;
            }
        }
        parser.accept(JSONToken.RBRACKET, JSONToken.COMMA);
    }
    
    public static boolean checkName(String name) {
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (c < '\001' || c > '\177') {
                return false;
            }
        }
        
        return true;
    }
}
