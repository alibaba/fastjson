/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class TypeUtils {

    public static boolean   compatibleWithJavaBean      = false;
    private static boolean  setAccessibleEnable         = true;

    private static boolean  oracleTimestampMethodInited = false;
    private static Method   oracleTimestampMethod;

    private static boolean  oracleDateMethodInited      = false;
    private static Method   oracleDateMethod;

    private static boolean  optionalClassInited         = false;
    private static Class<?> optionalClass;

    static {
        try {
            String prop = System.getProperty("fastjson.compatibleWithJavaBean");
            if ("true".equals(prop)) {
                compatibleWithJavaBean = true;
            } else if ("false".equals(prop)) {
                compatibleWithJavaBean = false;
            }
        } catch (Throwable ex) {
            // skip
        }
    }

    public static String castToString(Object value) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Byte.parseByte(strVal);
        }

        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Character) {
            return (Character) value;
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if (strVal.length() != 1) {
                throw new JSONException("can not cast to char, value : " + value);
            }

            return strVal.charAt(0);
        }

        throw new JSONException("can not cast to char, value : " + value);
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Short.parseShort(strVal);
        }

        throw new JSONException("can not cast to short, value : " + value);
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }

        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }

        return new BigDecimal(strVal);
    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }

        if (value instanceof Float || value instanceof Double) {
            return BigInteger.valueOf(((Number) value).longValue());
        }

        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }

        return new BigInteger(strVal);
    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }

        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Float.parseFloat(strVal);
        }

        throw new JSONException("can not cast to float, value : " + value);
    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Double.parseDouble(strVal);
        }

        throw new JSONException("can not cast to double, value : " + value);
    }

    public static Date castToDate(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof Date) { // 使用频率最高的，应优先处理
        	return (Date) value;
        }

        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }

        long longValue = -1;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
            return new Date(longValue);
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.indexOf('-') != -1) {
                String format;
                if (strVal.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                    format = JSON.DEFFAULT_DATE_FORMAT;
                } else if (strVal.length() == 10) {
                    format = "yyyy-MM-dd";
                } else if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                    format = "yyyy-MM-dd HH:mm:ss";
                } else {
                    format = "yyyy-MM-dd HH:mm:ss.SSS";
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                try {
                    return (Date) dateFormat.parse(strVal);
                } catch (ParseException e) {
                    throw new JSONException("can not cast to Date, value : " + strVal);
                }
            }

            if (strVal.length() == 0) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue < 0) {
            Class<?> clazz = value.getClass();
            if ("oracle.sql.TIMESTAMP".equals(clazz.getName())) {
                if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                    try {
                        oracleTimestampMethod = clazz.getMethod("toJdbc");
                    } catch (NoSuchMethodException e) {
                        // skip
                    } finally {
                        oracleTimestampMethodInited = true;
                    }
                }

                Object result;
                try {
                    result = oracleTimestampMethod.invoke(value);
                } catch (Exception e) {
                    throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e);
                }
                return (Date) result;
            }

            if ("oracle.sql.DATE".equals(clazz.getName())) {
                if (oracleDateMethod == null && !oracleDateMethodInited) {
                    try {
                        oracleDateMethod = clazz.getMethod("toJdbc");
                    } catch (NoSuchMethodException e) {
                        // skip
                    } finally {
                        oracleDateMethodInited = true;
                    }
                }

                Object result;
                try {
                    result = oracleDateMethod.invoke(value);
                } catch (Exception e) {
                    throw new JSONException("can not cast oracle.sql.DATE to Date", e);
                }
                return (Date) result;
            }

            throw new JSONException("can not cast to Date, value : " + value);
        }

        return new Date(longValue);
    }

    public static java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }
        
        if (value instanceof java.sql.Date) {
        	return (java.sql.Date) value;
        }
        
        if (value instanceof java.util.Date) {
        	return new java.sql.Date(((java.util.Date) value).getTime());
        }

        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue <= 0) {
            throw new JSONException("can not cast to Date, value : " + value); // TODO 忽略 1970-01-01 之前的时间处理？
        }

        return new java.sql.Date(longValue);
    }

    public static java.sql.Timestamp castToTimestamp(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return new java.sql.Timestamp(((Calendar) value).getTimeInMillis());
        }

        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }

        if (value instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) value).getTime());
        }

        long longValue = 0;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue <= 0) {
            throw new JSONException("can not cast to Date, value : " + value);
        }

        return new java.sql.Timestamp(longValue);
    }

    public static Long castToLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            try {
                return Long.parseLong(strVal);
            } catch (NumberFormatException ex) {
                //
            }

            JSONScanner dateParser = new JSONScanner(strVal);
            Calendar calendar = null;
            if (dateParser.scanISO8601DateIfMatch(false)) {
                calendar = dateParser.getCalendar();
            }
            dateParser.close();

            if (calendar != null) {
                return calendar.getTimeInMillis();
            }
        }

        throw new JSONException("can not cast to long, value : " + value);
    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Integer.parseInt(strVal);
        }

        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? 1 : 0;
        }

        throw new JSONException("can not cast to int, value : " + value);
    }

    public static byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        if (value instanceof String) {
            return IOUtils.decodeFast((String) value);
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }

        if (value instanceof String) {
            String strVal = (String) value;

            if (strVal.length() == 0) {
                return null;
            }

            if ("true".equalsIgnoreCase(strVal)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal)) {
                return Boolean.FALSE;
            }

            if ("1".equals(strVal)) {
                return Boolean.TRUE;
            }

            if ("0".equals(strVal)) {
                return Boolean.FALSE;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
        }

        throw new JSONException("can not cast to boolean, value : " + value);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> clazz) {
        return cast(obj, clazz, ParserConfig.getGlobalInstance());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T cast(Object obj, Class<T> clazz, ParserConfig mapping) {
        if (obj == null) {
            return null;
        }

        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        if (clazz == obj.getClass()) {
            return (T) obj;
        }

        if (obj instanceof Map) {
            if (clazz == Map.class) {
                return (T) obj;
            }

            Map map = (Map) obj;
            if (clazz == Object.class && !map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                return (T) obj;
            }

            return castToJavaBean((Map<String, Object>) obj, clazz, mapping);
        }

        if (clazz.isArray()) {
            if (obj instanceof Collection) {

                Collection collection = (Collection) obj;
                int index = 0;
                Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                for (Object item : collection) {
                    Object value = cast(item, clazz.getComponentType(), mapping);
                    Array.set(array, index, value);
                    index++;
                }

                return (T) array;
            }

            if (clazz == byte[].class) {
                return (T) castToBytes(obj);
            }
        }

        if (clazz.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) castToBoolean(obj);
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) castToByte(obj);
        }

        // if (clazz == char.class || clazz == Character.class) {
        // return (T) castToCharacter(obj);
        // }

        if (clazz == short.class || clazz == Short.class) {
            return (T) castToShort(obj);
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) castToInt(obj);
        }

        if (clazz == long.class || clazz == Long.class) {
            return (T) castToLong(obj);
        }

        if (clazz == float.class || clazz == Float.class) {
            return (T) castToFloat(obj);
        }

        if (clazz == double.class || clazz == Double.class) {
            return (T) castToDouble(obj);
        }

        if (clazz == String.class) {
            return (T) castToString(obj);
        }

        if (clazz == BigDecimal.class) {
            return (T) castToBigDecimal(obj);
        }

        if (clazz == BigInteger.class) {
            return (T) castToBigInteger(obj);
        }

        if (clazz == Date.class) {
            return (T) castToDate(obj);
        }

        if (clazz == java.sql.Date.class) {
            return (T) castToSqlDate(obj);
        }

        if (clazz == java.sql.Timestamp.class) {
            return (T) castToTimestamp(obj);
        }

        if (clazz.isEnum()) {
            return (T) castToEnum(obj, clazz, mapping);
        }

        if (Calendar.class.isAssignableFrom(clazz)) {
            Date date = castToDate(obj);
            Calendar calendar;
            if (clazz == Calendar.class) {
                calendar = Calendar.getInstance();
            } else {
                try {
                    calendar = (Calendar) clazz.newInstance();
                } catch (Exception e) {
                    throw new JSONException("can not cast to : " + clazz.getName(), e);
                }
            }
            calendar.setTime(date);
            return (T) calendar;
        }

        if (obj instanceof String) {
            String strVal = (String) obj;

            if (strVal.length() == 0) {
                return null;
            }

            if (clazz == java.util.Currency.class) {
                return (T) java.util.Currency.getInstance(strVal);
            }
        }

        throw new JSONException("can not cast to : " + clazz.getName());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T castToEnum(Object obj, Class<T> clazz, ParserConfig mapping) {
        try {
            if (obj instanceof String) {
                String name = (String) obj;
                if (name.length() == 0) {
                    return null;
                }

                return (T) Enum.valueOf((Class<? extends Enum>) clazz, name);
            }

            if (obj instanceof Number) {
                int ordinal = ((Number) obj).intValue();

                Method method = clazz.getMethod("values");
                Object[] values = (Object[]) method.invoke(null);
                for (Object value : values) {
                    Enum e = (Enum) value;
                    if (e.ordinal() == ordinal) {
                        return (T) e;
                    }
                }
            }
        } catch (Exception ex) {
            throw new JSONException("can not cast to : " + clazz.getName(), ex);
        }

        throw new JSONException("can not cast to : " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Type type, ParserConfig mapping) {
        if (obj == null) {
            return null;
        }

        if (type instanceof Class) {
            return (T) cast(obj, (Class<T>) type, mapping);
        }

        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, mapping);
        }

        if (obj instanceof String) {
            String strVal = (String) obj;
            if (strVal.length() == 0) {
                return null;
            }
        }

        if (type instanceof TypeVariable) {
            return (T) obj;
        }

        throw new JSONException("can not cast to : " + type);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T cast(Object obj, ParameterizedType type, ParserConfig mapping) {
        Type rawTye = type.getRawType();

        if (rawTye == Set.class || rawTye == HashSet.class //
            || rawTye == TreeSet.class //
            || rawTye == List.class //
            || rawTye == ArrayList.class) {
            Type itemType = type.getActualTypeArguments()[0];

            if (obj instanceof Iterable) {
                Collection collection;
                if (rawTye == Set.class || rawTye == HashSet.class) {
                    collection = new HashSet();
                } else if (rawTye == TreeSet.class) {
                    collection = new TreeSet();
                } else {
                    collection = new ArrayList();
                }

                for (Iterator it = ((Iterable) obj).iterator(); it.hasNext();) {
                    Object item = it.next();
                    collection.add(cast(item, itemType, mapping));
                }

                return (T) collection;
            }
        }

        if (rawTye == Map.class || rawTye == HashMap.class) {
            Type keyType = type.getActualTypeArguments()[0];
            Type valueType = type.getActualTypeArguments()[1];

            if (obj instanceof Map) {
                Map map = new HashMap();

                for (Map.Entry entry : ((Map<?, ?>) obj).entrySet()) {
                    Object key = cast(entry.getKey(), keyType, mapping);
                    Object value = cast(entry.getValue(), valueType, mapping);

                    map.put(key, value);
                }

                return (T) map;
            }
        }

        if (obj instanceof String) {
            String strVal = (String) obj;
            if (strVal.length() == 0) {
                return null;
            }
        }

        if (type.getActualTypeArguments().length == 1) {
            Type argType = type.getActualTypeArguments()[0];
            if (argType instanceof WildcardType) {
                return (T) cast(obj, rawTye, mapping);
            }
        }

        throw new JSONException("can not cast to : " + type);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T> T castToJavaBean(Map<String, Object> map, Class<T> clazz, ParserConfig config) {
        try {
            if (clazz == StackTraceElement.class) {
                String declaringClass = (String) map.get("className");
                String methodName = (String) map.get("methodName");
                String fileName = (String) map.get("fileName");
                int lineNumber;
                {
                    Number value = (Number) map.get("lineNumber");
                    if (value == null) {
                        lineNumber = 0;
                    } else {
                        lineNumber = value.intValue();
                    }
                }

                return (T) new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
            }

            {
                Object iClassObject = map.get(JSON.DEFAULT_TYPE_KEY);
                if (iClassObject instanceof String) {
                    String className = (String) iClassObject;

                    Class<?> loadClazz = (Class<T>) loadClass(className);

                    if (loadClazz == null) {
                        throw new ClassNotFoundException(className + " not found");
                    }

                    if (!loadClazz.equals(clazz)) {
                        return (T) castToJavaBean(map, loadClazz, config);
                    }
                }
            }

            if (clazz.isInterface()) {
                JSONObject object;

                if (map instanceof JSONObject) {
                    object = (JSONObject) map;
                } else {
                    object = new JSONObject(map);
                }

                return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                                                  new Class<?>[] { clazz }, object);
            }

            if (config == null) {
                config = ParserConfig.getGlobalInstance();
            }

            JavaBeanDeserializer javaBeanDeser = null;
            ObjectDeserializer deserizer = config.getDeserializer(clazz);
            if (deserizer instanceof JavaBeanDeserializer) {
                javaBeanDeser = (JavaBeanDeserializer) deserizer;
            } else if (deserizer instanceof ASMJavaBeanDeserializer) {
                javaBeanDeser = ((ASMJavaBeanDeserializer) deserizer).getInnterSerializer();
            }

            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T object = constructor.newInstance();

            
            if (javaBeanDeser != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    
                    FieldDeserializer fieldDeser = javaBeanDeser.getFieldDeserializer(key);
                    if (fieldDeser == null) {
                        continue;
                    }
                    
                    Method method = fieldDeser.fieldInfo.method;
                    if (method != null) {
                        Type paramType = method.getGenericParameterTypes()[0];
                        value = cast(value, paramType, config);
                        method.invoke(object, new Object[] { value });
                    } else {
                        Field field = fieldDeser.fieldInfo.field;
                        Type paramType = fieldDeser.fieldInfo.fieldType;
                        value = cast(value, paramType, config);
                        field.set(object, value);
                    }
                }
            }

            return object;
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap<String, Class<?>>();

    static {
        addBaseClassMappings();
    }

    public static void addClassMapping(String className, Class<?> clazz) {
        if (className == null) {
            className = clazz.getName();
        }

        mappings.put(className, clazz);
    }

    public static void addBaseClassMappings() {
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

        mappings.put(HashMap.class.getName(), HashMap.class);
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className, null);
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        if (className == null || className.length() == 0) {
            return null;
        }

        Class<?> clazz = mappings.get(className);

        if (clazz != null) {
            return clazz;
        }

        if (className.charAt(0) == '[') {
            Class<?> componentType = loadClass(className.substring(1), classLoader);
            return Array.newInstance(componentType, 0).getClass();
        }

        if (className.startsWith("L") && className.endsWith(";")) {
            String newClassName = className.substring(1, className.length() - 1);
            return loadClass(newClassName, classLoader);
        }
        
        try {
            if (classLoader != null) {
                clazz = classLoader.loadClass(className);

                addClassMapping(className, clazz);

                return clazz;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            // skip
        }

        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

            if (contextClassLoader != null) {
                clazz = contextClassLoader.loadClass(className);

                addClassMapping(className, clazz);

                return clazz;
            }
        } catch (Throwable e) {
            // skip
        }

        try {
            clazz = Class.forName(className);

            addClassMapping(className, clazz);

            return clazz;
        } catch (Throwable e) {
            // skip
        }

        return clazz;
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, JSONType jsonType, Map<String, String> aliasMap, boolean sorted) {
        Map<String, FieldInfo> fieldInfoMap = new LinkedHashMap<String, FieldInfo>();

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            int ordinal = 0, serialzeFeatures = 0;
            String label = null;

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.getReturnType().equals(Void.TYPE)) {
                continue;
            }

            if (method.getParameterTypes().length != 0) {
                continue;
            }

            if (method.getReturnType() == ClassLoader.class) {
                continue;
            }

            if (method.getName().equals("getMetaClass")
                && method.getReturnType().getName().equals("groovy.lang.MetaClass")) {
                continue;
            }

            JSONField annotation = method.getAnnotation(JSONField.class);

            if (annotation == null) {
                annotation = getSupperMethodAnnotation(clazz, method);
            }

            if (annotation != null) {
                if (!annotation.serialize()) {
                    continue;
                }

                ordinal = annotation.ordinal();
                serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());

                if (annotation.name().length() != 0) {
                    String propertyName = annotation.name();

                    if (aliasMap != null) {
                        propertyName = aliasMap.get(propertyName);
                        if (propertyName == null) {
                            continue;
                        }
                    }

                    FieldInfo fieldInfo = new FieldInfo(propertyName, method, null, clazz, null, ordinal, serialzeFeatures, annotation, null, label);
                    fieldInfoMap.put(propertyName, fieldInfo);
                    continue;
                }

                if (annotation.label().length() != 0) {
                    label = annotation.label();
                }
            }

            if (methodName.startsWith("get")) {
                if (methodName.length() < 4) {
                    continue;
                }

                if (methodName.equals("getClass")) {
                    continue;
                }

                char c3 = methodName.charAt(3);

                String propertyName;
                if (Character.isUpperCase(c3) //
                    || c3 > 512 // for unicode method name
                ) {
                    if (compatibleWithJavaBean) {
                        propertyName = decapitalize(methodName.substring(3));
                    } else {
                        propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    }
                } else if (c3 == '_') {
                    propertyName = methodName.substring(4);
                } else if (c3 == 'f') {
                    propertyName = methodName.substring(3);
                } else if (methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))) {
                    propertyName = decapitalize(methodName.substring(3));
                } else {
                    continue;
                }

                boolean ignore = isJSONTypeIgnore(clazz, propertyName);

                if (ignore) {
                    continue;
                }

                Field field = ParserConfig.getField(clazz, propertyName);
                JSONField fieldAnnotation = null;
                if (field != null) {
                    fieldAnnotation = field.getAnnotation(JSONField.class);

                    if (fieldAnnotation != null) {
                        if (!fieldAnnotation.serialize()) {
                            continue;
                        }

                        ordinal = fieldAnnotation.ordinal();
                        serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());

                        if (fieldAnnotation.name().length() != 0) {
                            propertyName = fieldAnnotation.name();

                            if (aliasMap != null) {
                                propertyName = aliasMap.get(propertyName);
                                if (propertyName == null) {
                                    continue;
                                }
                            }
                        }

                        if (fieldAnnotation.label().length() != 0) {
                            label = fieldAnnotation.label();
                        }
                    }
                }

                if (aliasMap != null) {
                    propertyName = aliasMap.get(propertyName);
                    if (propertyName == null) {
                        continue;
                    }
                }

                FieldInfo fieldInfo = new FieldInfo(propertyName, method, field, clazz, null, ordinal, serialzeFeatures, annotation, fieldAnnotation, label);
                fieldInfoMap.put(propertyName, fieldInfo);
            }

            if (methodName.startsWith("is")) {
                if (methodName.length() < 3) {
                    continue;
                }

                char c2 = methodName.charAt(2);

                String propertyName;
                if (Character.isUpperCase(c2)) {
                    if (compatibleWithJavaBean) {
                        propertyName = decapitalize(methodName.substring(2));
                    } else {
                        propertyName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
                    }
                } else if (c2 == '_') {
                    propertyName = methodName.substring(3);
                } else if (c2 == 'f') {
                    propertyName = methodName.substring(2);
                } else {
                    continue;
                }

                Field field = ParserConfig.getField(clazz, propertyName);

                if (field == null) {
                    field = ParserConfig.getField(clazz, methodName);
                }

                JSONField fieldAnnotation = null;
                if (field != null) {
                    fieldAnnotation = field.getAnnotation(JSONField.class);

                    if (fieldAnnotation != null) {
                        if (!fieldAnnotation.serialize()) {
                            continue;
                        }

                        ordinal = fieldAnnotation.ordinal();
                        serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());

                        if (fieldAnnotation.name().length() != 0) {
                            propertyName = fieldAnnotation.name();

                            if (aliasMap != null) {
                                propertyName = aliasMap.get(propertyName);
                                if (propertyName == null) {
                                    continue;
                                }
                            }
                        }

                        if (fieldAnnotation.label().length() != 0) {
                            label = fieldAnnotation.label();
                        }
                    }
                }

                if (aliasMap != null) {
                    propertyName = aliasMap.get(propertyName);
                    if (propertyName == null) {
                        continue;
                    }
                }

                FieldInfo fieldInfo = new FieldInfo(propertyName, method, field, clazz, null, ordinal, serialzeFeatures, annotation, fieldAnnotation, label);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }

        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

            int ordinal = 0, serialzeFeatures = 0;
            String propertyName = field.getName();
            String label = null;
            if (fieldAnnotation != null) {
                if (!fieldAnnotation.serialize()) {
                    continue;
                }

                ordinal = fieldAnnotation.ordinal();
                serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());

                if (fieldAnnotation.name().length() != 0) {
                    propertyName = fieldAnnotation.name();
                }

                if (fieldAnnotation.label().length() != 0) {
                    label = fieldAnnotation.label();
                }
            }

            if (aliasMap != null) {
                propertyName = aliasMap.get(propertyName);
                if (propertyName == null) {
                    continue;
                }
            }

            if (!fieldInfoMap.containsKey(propertyName)) {
                FieldInfo fieldInfo = new FieldInfo(propertyName, null, field, clazz, null, ordinal, serialzeFeatures, null, fieldAnnotation, label);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }

        List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();

        boolean containsAll = false;
        String[] orders = null;

        JSONType annotation = clazz.getAnnotation(JSONType.class);
        if (annotation != null) {
            orders = annotation.orders();

            if (orders != null && orders.length == fieldInfoMap.size()) {
                containsAll = true;
                for (String item : orders) {
                    if (!fieldInfoMap.containsKey(item)) {
                        containsAll = false;
                        break;
                    }
                }
            } else {
                containsAll = false;
            }
        }

        if (containsAll) {
            for (String item : orders) {
                FieldInfo fieldInfo = fieldInfoMap.get(item);
                fieldInfoList.add(fieldInfo);
            }
        } else {
            for (FieldInfo fieldInfo : fieldInfoMap.values()) {
                fieldInfoList.add(fieldInfo);
            }

            if (sorted) {
                Collections.sort(fieldInfoList);
            }
        }

        return fieldInfoList;
    }

    public static JSONField getSupperMethodAnnotation(final Class<?> clazz, final Method method) {
    	Class<?>[] interfaces = clazz.getInterfaces();
    	if (interfaces.length > 0) {
    		Class<?>[] types = method.getParameterTypes(); 
    		for (Class<?> interfaceClass : interfaces) {
                for (Method interfaceMethod : interfaceClass.getMethods()) {
                	Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes(); 
                	if (interfaceTypes.length != types.length) {
                		continue;					
					}
                    if (!interfaceMethod.getName().equals(method.getName())) {
                        continue;
                    }
                    boolean match = true;
                    for (int i = 0; i < types.length; ++i) {
                        if (!interfaceTypes[i].equals(types[i])) {
                            match = false;
                            break;
                        }
                    }

                    if (!match) {
                        continue;
                    }

                    JSONField annotation = interfaceMethod.getAnnotation(JSONField.class);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
		}
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> clazz, String propertyName) {
        JSONType jsonType = clazz.getAnnotation(JSONType.class);

        if (jsonType != null) {
            // 1、新增 includes 支持，如果 JSONType 同时设置了includes 和 ignores 属性，则以includes为准。
            // 2、个人认为对于大小写敏感的Java和JS而言，使用 equals() 比 equalsIgnoreCase() 更好，改动的唯一风险就是向后兼容性的问题
            // 不过，相信开发者应该都是严格按照大小写敏感的方式进行属性设置的
            String[] fields = jsonType.includes();
            if (fields.length > 0) {
                for (int i = 0; i < fields.length; i++) {
                    if (propertyName.equals(fields[i])) {
                        return false;
                    }
                }
                return true;
            } else {
                fields = jsonType.ignores();
                for (int i = 0; i < fields.length; i++) {
                    if (propertyName.equals(fields[i])) {
                        return true;
                    }
                }
            }
        }

        if (clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
            if (isJSONTypeIgnore(clazz.getSuperclass(), propertyName)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return true;
        }

        if (type instanceof Class) {
            Type superType = ((Class<?>) type).getGenericSuperclass();
            if (superType == Object.class) {
                return false;
            }
            return isGenericParamType(superType);
        }

        return false;
    }

    public static Type getGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return type;
        }

        if (type instanceof Class) {
            return getGenericParamType(((Class<?>) type).getGenericSuperclass());
        }

        return type;
    }

    public static Type unwrap(Type type) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType == byte.class) {
                return byte[].class;
            }
            if (componentType == char.class) {
                return char[].class;
            }
        }

        return type;
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception e) {
                // skip
            } finally {
                optionalClassInited = true;
            }
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (parameterizedType.getRawType() == optionalClass) {
                return parameterizedType.getActualTypeArguments()[0];
            }
        }
        return type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        
        if (type instanceof TypeVariable) {
            Type boundType = ((TypeVariable<?>) type).getBounds()[0];
            return (Class<?>) boundType;
        }

        return Object.class;
    }

    public static Field getField(Class<?> clazz, String fieldName, Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return getField(superClass, fieldName, superClass.getDeclaredFields());
        }

        return null;
    }

    public static JSONType getJSONType(Class<?> clazz) {
        return clazz.getAnnotation(JSONType.class);
    }

    public static int getSerializeFeatures(Class<?> clazz) {
        JSONType annotation = clazz.getAnnotation(JSONType.class);

        if (annotation == null) {
            return 0;
        }

        return SerializerFeature.of(annotation.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> clazz) {
        JSONType annotation = clazz.getAnnotation(JSONType.class);

        if (annotation == null) {
            return 0;
        }

        return Feature.of(annotation.parseFeatures());
    }

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    static void setAccessible(AccessibleObject obj) {
        if (!setAccessibleEnable) {
            return;
        }

        if (obj.isAccessible()) {
            return;
        }

        try {
            obj.setAccessible(true);
        } catch (AccessControlException error) {
            setAccessibleEnable = false;
        }
    }

    public static Class<?> getCollectionItemClass(Type fieldType) {
        if (fieldType instanceof ParameterizedType) {
            Class<?> itemClass;
            Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];

            if (actualTypeArgument instanceof Class) {
                itemClass = (Class<?>) actualTypeArgument;
                if (!Modifier.isPublic(itemClass.getModifiers())) {
                    throw new JSONException("can not create ASMParser");
                }
            } else {
                throw new JSONException("can not create ASMParser");
            }
            return itemClass;
        }

        return Object.class;
    }

}
