package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class TypeUtilsMappingTest extends TestCase {
    public void test_mapping() throws Exception {
        Map<String, Class<?>> mappings = new HashMap<String, Class<?>>();
        mappings.put("byte", byte.class);
        mappings.put("short", short.class);
        mappings.put("int", int.class);
        mappings.put("long", long.class);
        mappings.put("float", float.class);
        mappings.put("double", double.class);
        mappings.put("boolean", boolean.class);
        mappings.put("char", char.class);

        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);

        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);

        mappings.put("java.util.HashMap", HashMap.class);
        mappings.put("java.util.TreeMap", TreeMap.class);
        mappings.put("java.util.Date", java.util.Date.class);
        mappings.put("com.alibaba.fastjson.JSONObject", JSONObject.class);
        mappings.put("java.util.concurrent.ConcurrentHashMap", ConcurrentHashMap.class);
        mappings.put("java.text.SimpleDateFormat", SimpleDateFormat.class);
        mappings.put("java.lang.StackTraceElement", StackTraceElement.class);
        mappings.put("java.lang.RuntimeException", RuntimeException.class);

        Map<Long, Class<?>> fnvMapping = new HashMap<Long, Class<?>>();

        for (Map.Entry<String, Class<?>> entry : mappings.entrySet()) {
            long hash = fnv(entry.getKey());
            fnvMapping.put(hash, entry.getValue());
        }

        long[] hashCodes = new long[fnvMapping.size()];
        int p = 0;
        for (Long key : fnvMapping.keySet()) {
            hashCodes[p++] = key;
        }
        Arrays.sort(hashCodes);

        Class<?>[] classes = new Class[hashCodes.length];
        for (int i = 0; i < hashCodes.length; i++) {
            long hash = hashCodes[i];
            classes[i] = fnvMapping.get(hash);
        }
//
//        Field field0 = TypeUtils.class.getDeclaredField("mappingClasses");
//        Field field0 = TypeUtils.class.getDeclaredField("mappingClasses");

//        for (int i = 0; i < hashCodes.length; i++) {
//            if (i != 0) {
//                System.out.printf(", ");
//            }
//            long hash = hashCodes[i];
//            System.out.println(hash + "L");
//        }
//
//        for (int i = 0; i < classes.length; i++) {
//            if (i != 0) {
//                System.out.printf(", ");
//            }
//
//            Class cls = classes[i];
//            String name;
//            if (cls.isArray()) {
//                name = cls.getComponentType().getName() + "[].class";
//            } else {
//                name = cls.getName() + ".class";
//            }
//            System.out.println(name);
//        }
    }


    public static long fnv(String name) {
        long hashCode = 0xcbf29ce484222325L;
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            hashCode ^= c;
            hashCode *= 0x100000001b3L;
        }
        return hashCode;
    }
}
