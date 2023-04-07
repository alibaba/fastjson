package com.alibaba.fastjson.asm;

import com.alibaba.fastjson.util.ASMUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;



public abstract class BaseCollector {
    protected static final String JSON_TYPE = ASMUtils.desc(com.alibaba.fastjson.annotation.JSONType.class);

    protected static final Map<String, String> PRIMITIVES = new HashMap<String, String>() {
        {
            put("int","I");
            put("boolean","Z");
            put("byte", "B");
            put("char","C");
            put("short","S");
            put("float","F");
            put("long","J");
            put("double","D");
        }
    };

    protected final String methodName;

    protected final Class<?>[] parameterTypes;

    protected MethodCollector collector;

    protected boolean jsonType;

    protected BaseCollector(String methodName, Class<?>[] parameterTypes) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.collector = null;
    }

    protected abstract MethodCollector visitMethod(int access, String name, String desc);

    public void visitAnnotation(String desc) {
        if (JSON_TYPE.equals(desc)) {
            jsonType = true;
        }
    }

    protected boolean correctTypeName(Type type, String paramTypeName) {
        String s = type.getClassName();
        // array notation needs cleanup.
        StringBuilder braces = new StringBuilder();
        while (s.endsWith("[]")) {
            braces.append('[');
            s = s.substring(0, s.length() - 2);
        }
        if (braces.length() != 0) {
            if (PRIMITIVES.containsKey(s)) {
                s = braces.append(PRIMITIVES.get(s)).toString();
            } else {
                s = braces.append('L').append(s).append(';').toString();
            }
        }
        return s.equals(paramTypeName);
    }

    public String[] getParameterNamesForMethod() {
        if (collector == null || !collector.debugInfoPresent) {
            return new String[0];
        }
        return collector.getResult().split(",");
    }

    public boolean matched() {
        return collector != null;
    }

    public boolean hasJsonType() {
        return jsonType;
    }
}
