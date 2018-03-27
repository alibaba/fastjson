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
package com.alibaba.fastjson.parser;

import java.io.Closeable;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ArrayCodec;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.NumberCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {
    private static long[] denyList = new long[] {
            -7600952144447537354L,
            -4082057040235125754L,
            -2364987994247679115L,
            -676156662527871184L,
            -254670111376247151L,
            1502845958873959152L,
            4147696707147271408L,
            5347909877633654828L,
            5751393439502795295L,
            7702607466162283393L
    };

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public static ParserConfig                        global      = new ParserConfig();
    private final IdentityHashMap<ObjectDeserializer> deserializers = new IdentityHashMap<ObjectDeserializer>(1024);
    public final  SymbolTable                         symbolTable = new SymbolTable(16384);
    public        ClassLoader                         defaultClassLoader;
    public        PropertyNamingStrategy              propertyNamingStrategy;
    public        boolean                             autoTypeSupport;

    public ParserConfig(){
        deserializers.put(SimpleDateFormat.class, MiscCodec.instance);
        deserializers.put(java.util.Date.class, DateCodec.instance);
        deserializers.put(Calendar.class, DateCodec.instance);

        deserializers.put(Map.class, MapDeserializer.instance);
        deserializers.put(HashMap.class, MapDeserializer.instance);
        deserializers.put(LinkedHashMap.class, MapDeserializer.instance);
        deserializers.put(TreeMap.class, MapDeserializer.instance);
        deserializers.put(ConcurrentMap.class, MapDeserializer.instance);
        deserializers.put(ConcurrentHashMap.class, MapDeserializer.instance);

        deserializers.put(Collection.class, CollectionCodec.instance);
        deserializers.put(List.class, CollectionCodec.instance);
        deserializers.put(ArrayList.class, CollectionCodec.instance);

        deserializers.put(Object.class, JavaObjectDeserializer.instance);
        deserializers.put(String.class, StringCodec.instance);
        deserializers.put(char.class, MiscCodec.instance);
        deserializers.put(Character.class, MiscCodec.instance);
        deserializers.put(byte.class, NumberCodec.instance);
        deserializers.put(Byte.class, NumberCodec.instance);
        deserializers.put(short.class, NumberCodec.instance);
        deserializers.put(Short.class, NumberCodec.instance);
        deserializers.put(int.class, IntegerCodec.instance);
        deserializers.put(Integer.class, IntegerCodec.instance);
        deserializers.put(long.class, IntegerCodec.instance);
        deserializers.put(Long.class, IntegerCodec.instance);
        deserializers.put(BigInteger.class, BigDecimalCodec.instance);
        deserializers.put(BigDecimal.class, BigDecimalCodec.instance);
        deserializers.put(float.class, NumberCodec.instance);
        deserializers.put(Float.class, NumberCodec.instance);
        deserializers.put(double.class, NumberCodec.instance);
        deserializers.put(Double.class, NumberCodec.instance);
        deserializers.put(boolean.class, BooleanCodec.instance);
        deserializers.put(Boolean.class, BooleanCodec.instance);
        deserializers.put(Class.class, MiscCodec.instance);
        deserializers.put(char[].class, ArrayCodec.instance);
        deserializers.put(Object[].class, ArrayCodec.instance);

        deserializers.put(UUID.class, MiscCodec.instance);
        deserializers.put(TimeZone.class, MiscCodec.instance);
        deserializers.put(Locale.class, MiscCodec.instance);
        deserializers.put(Currency.class, MiscCodec.instance);
        deserializers.put(URI.class, MiscCodec.instance);
        deserializers.put(URL.class, MiscCodec.instance);
        deserializers.put(Pattern.class, MiscCodec.instance);
        deserializers.put(Charset.class, MiscCodec.instance);
        deserializers.put(Number.class, NumberCodec.instance);
        deserializers.put(StackTraceElement.class, MiscCodec.instance);

        deserializers.put(Serializable.class, JavaObjectDeserializer.instance);
        deserializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        deserializers.put(Comparable.class, JavaObjectDeserializer.instance);
        deserializers.put(Closeable.class, JavaObjectDeserializer.instance);

    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.deserializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type instanceof Class<?>) {
            return getDeserializer((Class<?>) type, type);
        }

        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class<?>) {
                return getDeserializer((Class<?>) rawType, type);
            } else {
                return getDeserializer(rawType);
            }
        }

        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length == 1) {
                Type upperBoundType = upperBounds[0];
                return getDeserializer(upperBoundType);
            }
        }

        return JavaObjectDeserializer.instance;
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer deserializer = deserializers.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        if (type == null) {
            type = clazz;
        }

        deserializer = deserializers.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        if (!isPrimitive(clazz)) {
            JSONType annotation = clazz.getAnnotation(JSONType.class);
            if (annotation != null) {
                Class<?> mappingTo = annotation.mappingTo();
                if (mappingTo != Void.class) {
                    return getDeserializer(mappingTo, mappingTo);
                }
            }
        }

        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof ParameterizedType) {
            deserializer = deserializers.get(clazz);
        }

        if (deserializer != null) {
            return deserializer;
        }

        deserializer = deserializers.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        if (clazz.isEnum()) {
            deserializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            deserializer = ArrayCodec.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class
                   || clazz == ArrayList.class) {
            deserializer = CollectionCodec.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            deserializer = CollectionCodec.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            deserializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            deserializer = new ThrowableDeserializer(this, clazz);
        } else {
            String clazzName = clazz.getName();

            if (clazzName.equals("android.net.Uri")) {
                deserializer = MiscCodec.instance;
            } else {
                deserializer = new JavaBeanDeserializer(this, clazz, type);
            }
        }

        putDeserializer(type, deserializer);

        return deserializer;
    }
    
    public ObjectDeserializer registerIfNotExists(Class<?> clazz) {
        return registerIfNotExists(clazz, clazz.getModifiers(), false, true, true, true);
    }
    
    public ObjectDeserializer registerIfNotExists(Class<?> clazz, // 
                                                  int classModifiers, // Class.getModifiers in android is slow
                                                  boolean fieldOnly, //
                                                  boolean jsonTypeSupport, //
                                                  boolean jsonFieldSupport, //
                                                  boolean fieldGenericSupport) {
        ObjectDeserializer deserializer = deserializers.get(clazz);
        if (deserializer != null) {
            return deserializer;
        }
        
        JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, // 
                                                   classModifiers, // 
                                                   clazz, fieldOnly, // 
                                                   jsonTypeSupport, // 
                                                   jsonFieldSupport, // 
                                                   fieldGenericSupport, //
                                                   propertyNamingStrategy);
        deserializer = new JavaBeanDeserializer(this, clazz, clazz, beanInfo);
        putDeserializer(clazz, deserializer);
        
        return deserializer;
    }

    public boolean containsKey(Class clazz) {
        return deserializers.get(clazz) != null;
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.fieldClass;

        if (fieldClass == List.class //
            || fieldClass == ArrayList.class //
            || (fieldClass.isArray() //
                && !fieldClass.getComponentType().isPrimitive()) //
        ) {
            return new ListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        deserializers.put(type, deserializer);
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() //
                 || clazz == Boolean.class //
                 || clazz == Character.class //
                 || clazz == Byte.class //
                 || clazz == Short.class //
                 || clazz == Integer.class //
                 || clazz == Long.class //
                 || clazz == Float.class //
                 || clazz == Double.class //
                 || clazz == BigInteger.class //
                 || clazz == BigDecimal.class //
                 || clazz == String.class //
                 || clazz == java.util.Date.class //
                 || clazz == java.sql.Date.class //
                 || clazz == java.sql.Time.class //
                 || clazz == java.sql.Timestamp.class //
                 ;
    }

    public Class<?> checkAutoType(String typeName, Class<?> expectClass, int features) {
        if (typeName == null) {
            return null;
        }

        if (typeName.length() >= 128 || typeName.length() < 3) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long BASIC = 0xcbf29ce484222325L;
        final long PRIME = 0x100000001b3L;

        final long h1 = (BASIC ^ typeName.charAt(0)) * PRIME;
        if (h1 == 0xaf64164c86024f1aL) { // [
            throw new JSONException("autoType is not support. " + typeName);
        }

        if ((h1 ^ typeName.charAt(typeName.length() - 1)) * PRIME == 0x9198507b5af98f0L) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long h3 = (((((BASIC ^ typeName.charAt(0))
                * PRIME)
                ^ typeName.charAt(1))
                * PRIME)
                ^ typeName.charAt(2))
                * PRIME;

        long hash = h3;
        for (int i = 3; i < typeName.length(); ++i) {
            hash ^= typeName.charAt(i);
            hash *= PRIME;
            if (Arrays.binarySearch(denyList, hash) >= 0 && TypeUtils.getClassFromMapping(typeName) == null) {
                throw new JSONException("autoType is not support. " + typeName);
            }
        }

        Class<?> clazz = TypeUtils.getClassFromMapping(typeName);
        if (clazz != null) {
            return clazz;
        }

        clazz = deserializers.findClass(typeName);
        if (clazz != null) {
            return clazz;
        }

        clazz = TypeUtils.loadClass(typeName, defaultClassLoader, false);

        if (clazz != null
            && expectClass != null
            && clazz != java.util.HashMap.class)
        {
            if (expectClass.isAssignableFrom(clazz)) {
                TypeUtils.addMapping(typeName, clazz);
                return clazz;
            } else {
                throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
            }
        }

        if (clazz.isAnnotationPresent(JSONType.class)) {
            TypeUtils.addMapping(typeName, clazz);
            return clazz;
        }

        final int mask = Feature.SupportAutoType.mask;
        if ((features & mask) == 0
                && (JSON.DEFAULT_PARSER_FEATURE & mask) == 0
                && !autoTypeSupport) {
            throw new JSONException("autoType is not support : " + typeName);
        }

        TypeUtils.addMapping(typeName, clazz);

        return clazz;
    }
}
