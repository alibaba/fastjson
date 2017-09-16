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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

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

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public static ParserConfig                        global      = new ParserConfig();
    private final IdentityHashMap<ObjectDeserializer> derializers = new IdentityHashMap<ObjectDeserializer>(1024);
    public final SymbolTable                          symbolTable = new SymbolTable(16384);
    public ClassLoader                                defaultClassLoader;
    public PropertyNamingStrategy                     propertyNamingStrategy;

    public ParserConfig(){
        derializers.put(SimpleDateFormat.class, MiscCodec.instance);
        derializers.put(java.util.Date.class, DateCodec.instance);
        derializers.put(Calendar.class, DateCodec.instance);

        derializers.put(Map.class, MapDeserializer.instance);
        derializers.put(HashMap.class, MapDeserializer.instance);
        derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        derializers.put(TreeMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);

        derializers.put(Collection.class, CollectionCodec.instance);
        derializers.put(List.class, CollectionCodec.instance);
        derializers.put(ArrayList.class, CollectionCodec.instance);

        derializers.put(Object.class, JavaObjectDeserializer.instance);
        derializers.put(String.class, StringCodec.instance);
        derializers.put(char.class, MiscCodec.instance);
        derializers.put(Character.class, MiscCodec.instance);
        derializers.put(byte.class, NumberCodec.instance);
        derializers.put(Byte.class, NumberCodec.instance);
        derializers.put(short.class, NumberCodec.instance);
        derializers.put(Short.class, NumberCodec.instance);
        derializers.put(int.class, IntegerCodec.instance);
        derializers.put(Integer.class, IntegerCodec.instance);
        derializers.put(long.class, IntegerCodec.instance);
        derializers.put(Long.class, IntegerCodec.instance);
        derializers.put(BigInteger.class, BigDecimalCodec.instance);
        derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        derializers.put(float.class, NumberCodec.instance);
        derializers.put(Float.class, NumberCodec.instance);
        derializers.put(double.class, NumberCodec.instance);
        derializers.put(Double.class, NumberCodec.instance);
        derializers.put(boolean.class, BooleanCodec.instance);
        derializers.put(Boolean.class, BooleanCodec.instance);
        derializers.put(Class.class, MiscCodec.instance);
        derializers.put(char[].class, ArrayCodec.instance);
        derializers.put(Object[].class, ArrayCodec.instance);

        derializers.put(UUID.class, MiscCodec.instance);
        derializers.put(TimeZone.class, MiscCodec.instance);
        derializers.put(Locale.class, MiscCodec.instance);
        derializers.put(Currency.class, MiscCodec.instance);
        derializers.put(URI.class, MiscCodec.instance);
        derializers.put(URL.class, MiscCodec.instance);
        derializers.put(Pattern.class, MiscCodec.instance);
        derializers.put(Charset.class, MiscCodec.instance);
        derializers.put(Number.class, NumberCodec.instance);
        derializers.put(StackTraceElement.class, MiscCodec.instance);

        derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        derializers.put(Closeable.class, JavaObjectDeserializer.instance);

    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.derializers.get(type);
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
        ObjectDeserializer deserializer = derializers.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        if (type == null) {
            type = clazz;
        }

        deserializer = derializers.get(type);
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
            deserializer = derializers.get(clazz);
        }

        if (deserializer != null) {
            return deserializer;
        }

        deserializer = derializers.get(type);
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
        ObjectDeserializer deserializer = derializers.get(clazz);
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
        return derializers.get(clazz) != null;
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
        derializers.put(type, deserializer);
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
}
