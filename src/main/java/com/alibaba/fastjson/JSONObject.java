/*
 * Copyright 1999-2017 Alibaba Group.
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
package com.alibaba.fastjson;

import static com.alibaba.fastjson.util.TypeUtils.castToBigDecimal;
import static com.alibaba.fastjson.util.TypeUtils.castToBigInteger;
import static com.alibaba.fastjson.util.TypeUtils.castToBoolean;
import static com.alibaba.fastjson.util.TypeUtils.castToByte;
import static com.alibaba.fastjson.util.TypeUtils.castToBytes;
import static com.alibaba.fastjson.util.TypeUtils.castToDate;
import static com.alibaba.fastjson.util.TypeUtils.castToDouble;
import static com.alibaba.fastjson.util.TypeUtils.castToFloat;
import static com.alibaba.fastjson.util.TypeUtils.castToInt;
import static com.alibaba.fastjson.util.TypeUtils.castToLong;
import static com.alibaba.fastjson.util.TypeUtils.castToShort;
import static com.alibaba.fastjson.util.TypeUtils.castToSqlDate;
import static com.alibaba.fastjson.util.TypeUtils.castToTimestamp;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable, InvocationHandler {

    private static final long         serialVersionUID         = 1L;
    private static final int          DEFAULT_INITIAL_CAPACITY = 16;

    private final Map<String, Object> map;

    public JSONObject(){
        this(DEFAULT_INITIAL_CAPACITY, false);
    }

    public JSONObject(Map<String, Object> map){
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JSONObject(boolean ordered){
        this(DEFAULT_INITIAL_CAPACITY, ordered);
    }

    public JSONObject(int initialCapacity){
        this(initialCapacity, false);
    }

    public JSONObject(int initialCapacity, boolean ordered){
        if (ordered) {
            map = new LinkedHashMap<String, Object>(initialCapacity);
        } else {
            map = new HashMap<String, Object>(initialCapacity);
        }
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        boolean result = map.containsKey(key);
        if (!result) {
            if (key instanceof Number
                    || key instanceof Character
                    || key instanceof Boolean
                    || key instanceof UUID
            ) {
                result = map.containsKey(key.toString());
            }
        }
        return result;
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Object get(Object key) {
        Object val = map.get(key);

        if (val == null) {
            if (key instanceof Number
                    || key instanceof Character
                    || key instanceof Boolean
                    || key instanceof UUID
            ) {
                val = map.get(key.toString());
            }
        }

        return val;
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        Object v;
        return ((v = get(key)) != null) ? v : defaultValue;
    }

    public JSONObject getJSONObject(String key) {
        Object value = map.get(key);

        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }

        return (JSONObject) toJSON(value);
    }

    public JSONArray getJSONArray(String key) {
        Object value = map.get(key);

        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }

        if (value instanceof List) {
            return new JSONArray((List) value);
        }

        if (value instanceof String) {
            return (JSONArray) JSON.parse((String) value);
        }

        return (JSONArray) toJSON(value);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        return TypeUtils.castToJavaBean(obj, clazz);
    }

    public <T> T getObject(String key, Type type) {
        Object obj = map.get(key);
        return TypeUtils.cast(obj, type, ParserConfig.getGlobalInstance());
    }

    public <T> T getObject(String key, TypeReference typeReference) {
        Object obj = map.get(key);
        if (typeReference == null) {
            return (T) obj;
        }
        return TypeUtils.cast(obj, typeReference.getType(), ParserConfig.getGlobalInstance());
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);
        // castToBoolean() supports null check
        return castToBoolean(value);
    }

    public byte[] getBytes(String key) {
        Object value = get(key);
        // castToBytes() supports null check
        return castToBytes(value);
    }

    public boolean getBooleanValue(String key) {
        Boolean booleanVal = getBoolean(key);
        return booleanVal != null && booleanVal;
    }

    public Byte getByte(String key) {
        Object value = get(key);

        return castToByte(value);
    }

    public byte getByteValue(String key) {
        Byte byteVal = getByte(key);
        return byteVal == null ? 0 : byteVal;
    }

    public Short getShort(String key) {
        Object value = get(key);

        return castToShort(value);
    }

    public short getShortValue(String key) {
        Short shortVal = getShort(key);
        return shortVal == null ? 0 : shortVal;
    }

    public Integer getInteger(String key) {
        Object value = get(key);

        return castToInt(value);
    }

    public int getIntValue(String key) {
        Integer intVal = getInteger(key);
        return intVal == null ? 0 : intVal;
    }

    public Long getLong(String key) {
        Object value = get(key);

        return castToLong(value);
    }

    public long getLongValue(String key) {
        Long longVal = getLong(key);
        return longVal == null ? 0L : longVal;
    }

    public Float getFloat(String key) {
        Object value = get(key);

        return castToFloat(value);
    }

    public float getFloatValue(String key) {
        Float floatValue = getFloat(key);
        return floatValue == null ? 0F : floatValue;
    }

    public Double getDouble(String key) {
        Object value = get(key);

        return castToDouble(value);
    }

    public double getDoubleValue(String key) {
        Double doubleValue = getDouble(key);
        return doubleValue == null ? 0D : doubleValue;
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);

        return castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);

        return castToBigInteger(value);
    }

    public String getString(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public Date getDate(String key) {
        Object value = get(key);

        return castToDate(value);
    }

    public Object getSqlDate(String key) {
        Object value = get(key);

        return castToSqlDate(value);
    }

    public Object getTimestamp(String key) {
        Object value = get(key);

        return castToTimestamp(value);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public JSONObject fluentPut(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    public JSONObject fluentPutAll(Map<? extends String, ?> m) {
        map.putAll(m);
        return this;
    }

    public void clear() {
        map.clear();
    }

    public JSONObject fluentClear() {
        map.clear();
        return this;
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public JSONObject fluentRemove(Object key) {
        map.remove(key);
        return this;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public JSONObject clone() {
        return new JSONObject(map instanceof LinkedHashMap //
                ? new LinkedHashMap<String, Object>(map) //
                : new HashMap<String, Object>(map)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof JSONObject) {
            return this.map.equals(((JSONObject) obj).map);
        }

        return this.map.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            if (method.getName().equals("equals")) {
                return this.equals(args[0]);
            }

            Class<?> returnType = method.getReturnType();
            if (returnType != void.class) {
                throw new JSONException("illegal setter");
            }

            String name = null;
            JSONField annotation = TypeUtils.getAnnotation(method, JSONField.class);
            if (annotation != null) {
                if (annotation.name().length() != 0) {
                    name = annotation.name();
                }
            }

            if (name == null) {
                name = method.getName();

                if (!name.startsWith("set")) {
                    throw new JSONException("illegal setter");
                }

                name = name.substring(3);
                if (name.length() == 0) {
                    throw new JSONException("illegal setter");
                }
                name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            }

            map.put(name, args[0]);
            return null;
        }

        if (parameterTypes.length == 0) {
            Class<?> returnType = method.getReturnType();
            if (returnType == void.class) {
                throw new JSONException("illegal getter");
            }

            String name = null;
            JSONField annotation = TypeUtils.getAnnotation(method, JSONField.class);
            if (annotation != null) {
                if (annotation.name().length() != 0) {
                    name = annotation.name();
                }
            }

            if (name == null) {
                name = method.getName();
                if (name.startsWith("get")) {
                    name = name.substring(3);
                    if (name.length() == 0) {
                        throw new JSONException("illegal getter");
                    }
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                } else if (name.startsWith("is")) {
                    name = name.substring(2);
                    if (name.length() == 0) {
                        throw new JSONException("illegal getter");
                    }
                    name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                } else if (name.startsWith("hashCode")) {
                    return this.hashCode();
                } else if (name.startsWith("toString")) {
                    return this.toString();
                } else {
                    throw new JSONException("illegal getter");
                }
            }

            Object value = map.get(name);
            return TypeUtils.cast(value, method.getGenericReturnType(), ParserConfig.getGlobalInstance());
        }

        throw new UnsupportedOperationException(method.toGenericString());
    }

    public Map<String, Object> getInnerMap() {
        return this.map;
    }



    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        SecureObjectInputStream.ensureFields();
        if (SecureObjectInputStream.fields != null && !SecureObjectInputStream.fields_error) {
            ObjectInputStream secIn = new SecureObjectInputStream(in);
            try {
                secIn.defaultReadObject();
                return;
            } catch (java.io.NotActiveException e) {
                // skip
            }
        }

        in.defaultReadObject();
        for (Entry<String, Object> entry : map.entrySet()) {
            final Object key = entry.getKey();
            if (key != null) {
                ParserConfig.global.checkAutoType(key.getClass());
            }

            final Object value = entry.getValue();
            if (value != null) {
                ParserConfig.global.checkAutoType(value.getClass());
            }
        }
    }

    static class SecureObjectInputStream extends ObjectInputStream {
        static Field[] fields;
        static volatile boolean fields_error;

        static void ensureFields() {
            if (fields == null && !fields_error) {
                try {
                    final Field[] declaredFields = ObjectInputStream.class.getDeclaredFields();
                    String[] fieldNames = new String[]{"bin", "passHandle", "handles", "curContext"};
                    Field[] array = new Field[fieldNames.length];
                    for (int i = 0; i < fieldNames.length; i++) {
                        Field field = TypeUtils
                                .getField(ObjectInputStream.class
                                        , fieldNames[i]
                                        , declaredFields
                                );
                        field.setAccessible(true);
                        array[i] = field;
                    }
                    fields = array;
                } catch (Throwable error) {
                    fields_error = true;
                }
            }
        }

        public SecureObjectInputStream(ObjectInputStream in) throws IOException {
            super(in);
            try {
                for (final Field field : fields) {
                    final Object value = field.get(in);
                    field.set(this, value);
                }
            } catch (IllegalAccessException e) {
                fields_error = true;
            }
        }

        protected Class<?> resolveClass(ObjectStreamClass desc)
                throws IOException, ClassNotFoundException {
            String name = desc.getName();
            if (name.length() > 2) {
                int index = name.lastIndexOf('[');
                if (index != -1) {
                    name = name.substring(index + 1);
                }
                if (name.length() > 2 && name.charAt(0) == 'L' && name.charAt(name.length() - 1) == ';') {
                    name = name.substring(1, name.length() - 1);
                }

                if (TypeUtils.getClassFromMapping(name) == null) {
                    ParserConfig.global.checkAutoType(name, null, Feature.SupportAutoType.mask);
                }
            }
            return super.resolveClass(desc);
        }

        protected Class<?> resolveProxyClass(String[] interfaces)
                throws IOException, ClassNotFoundException {
            for (String interfacename : interfaces) {
                //检查是否处于黑名单
                if (TypeUtils.getClassFromMapping(interfacename) == null) {
                    ParserConfig.global.checkAutoType(interfacename, null);
                }
            }
            return super.resolveProxyClass(interfaces);
        }

        //Hack:默认构造方法会调用这个方法，重写此方法使用反射还原部分关键属性
        // StreamCorruptedException inherits IOException
        protected void readStreamHeader() throws IOException {

        }
    }

    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JSONObject.class || clazz == JSON.class) {
            return (T) this;
        }

        if (clazz == Object.class && !containsKey(JSON.DEFAULT_TYPE_KEY)) {
            return (T) this;
        }

        return TypeUtils.castToJavaBean(this, clazz, ParserConfig.getGlobalInstance());
    }

    public <T> T toJavaObject(Class<T> clazz, ParserConfig config, int features) {
        if (clazz == Map.class) {
            return (T) this;
        }

        if (clazz == Object.class && !containsKey(JSON.DEFAULT_TYPE_KEY)) {
            return (T) this;
        }

        return TypeUtils.castToJavaBean(this, clazz, config);
    }
}
