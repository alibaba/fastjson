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
package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * circular references detect
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class SerializeConfig {

    public final static SerializeConfig globalInstance = new SerializeConfig();
    
    public final static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }
    
    private final IdentityHashMap<ObjectSerializer> serializers;
    protected String                                typeKey = JSON.DEFAULT_TYPE_KEY;
    public PropertyNamingStrategy                   propertyNamingStrategy;

    public ObjectSerializer registerIfNotExists(Class<?> clazz) {
        return registerIfNotExists(clazz, clazz.getModifiers(), false, true, true, true);
    }
    
    public ObjectSerializer registerIfNotExists(Class<?> clazz, //
                                                int classModifers,
                                                boolean fieldOnly, //
                                                boolean jsonTypeSupport, // 
                                                boolean jsonFieldSupport, //
                                                boolean fieldGenericSupport) {
        ObjectSerializer serializer = serializers.get(clazz);
        if (serializer == null) {
            serializer = new JavaBeanSerializer(clazz, // 
                                                classModifers, // 
                                                null, // 
                                                fieldOnly, // 
                                                jsonTypeSupport, // 
                                                jsonFieldSupport, // 
                                                fieldGenericSupport, //
                                                propertyNamingStrategy
                                                );
            this.serializers.put(clazz, serializer);
        }
        
        return serializer;
    }

    public SerializeConfig(){
        serializers = new IdentityHashMap<ObjectSerializer>(1024);

        serializers.put(Boolean.class, BooleanCodec.instance);
        serializers.put(Character.class, MiscCodec.instance);
        serializers.put(Byte.class, IntegerCodec.instance);
        serializers.put(Short.class, IntegerCodec.instance);
        serializers.put(Integer.class, IntegerCodec.instance);
        serializers.put(Long.class, IntegerCodec.instance);
        serializers.put(Float.class, NumberCodec.instance);
        serializers.put(Double.class, NumberCodec.instance);
        serializers.put(Number.class, NumberCodec.instance);
        serializers.put(BigDecimal.class, BigDecimalCodec.instance);
        serializers.put(BigInteger.class, BigDecimalCodec.instance);
        serializers.put(String.class, StringCodec.instance);
        serializers.put(Object[].class, ArrayCodec.instance);
        serializers.put(Class.class, MiscCodec.instance);

        serializers.put(SimpleDateFormat.class, MiscCodec.instance);
        serializers.put(Locale.class, MiscCodec.instance);
        serializers.put(Currency.class, MiscCodec.instance);
        serializers.put(TimeZone.class, MiscCodec.instance);
        serializers.put(UUID.class, MiscCodec.instance);
        serializers.put(URI.class, MiscCodec.instance);
        serializers.put(URL.class, MiscCodec.instance);
        serializers.put(Pattern.class, MiscCodec.instance);
        serializers.put(Charset.class, MiscCodec.instance);
    }
    
    public ObjectSerializer get(Class<?> clazz) {
        ObjectSerializer writer = serializers.get(clazz);

        if (writer == null) {
            Class<?> superClass;
            if (Map.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, new MapSerializer());
            } else if (List.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, new ListSerializer());
            } else if (Collection.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, CollectionCodec.instance);
            } else if (Date.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, DateCodec.instance);
            } else if (JSONAware.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, MiscCodec.instance);
            } else if (JSONSerializable.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, MiscCodec.instance);
            } else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, MiscCodec.instance);
            } else if (clazz.isEnum() 
                    || ((superClass = clazz.getSuperclass()) != null && superClass != Object.class && superClass.isEnum())) {
                serializers.put(clazz, new EnumSerializer());
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                ObjectSerializer compObjectSerializer = get(componentType);
                serializers.put(clazz, new ArraySerializer(componentType, compObjectSerializer));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                JavaBeanSerializer serializer = new JavaBeanSerializer(clazz, propertyNamingStrategy);
                serializer.features |= SerializerFeature.WriteClassName.mask;
                serializers.put(clazz, serializer);
            } else if (TimeZone.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, MiscCodec.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, MiscCodec.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, MiscCodec.instance);
            } else if (Calendar.class.isAssignableFrom(clazz)) {
                serializers.put(clazz, DateCodec.instance);
            } else {
                boolean isCglibProxy = false;
                boolean isJavassistProxy = false;
                for (Class<?> item : clazz.getInterfaces()) {
                    if (item.getName().equals("net.sf.cglib.proxy.Factory")
                        || item.getName().equals("org.springframework.cglib.proxy.Factory")) {
                        isCglibProxy = true;
                        break;
                    } else if (item.getName().equals("javassist.util.proxy.ProxyObject")) {
                        isJavassistProxy = true;
                        break;
                    }
                }

                if (isCglibProxy || isJavassistProxy) {
                    Class<?> superClazz = clazz.getSuperclass();

                    ObjectSerializer superWriter = get(superClazz);
                    serializers.put(clazz, superWriter);
                    return superWriter;
                }

                String className = clazz.getName();

                if (className.startsWith("android.net.Uri$")) {
                    serializers.put(clazz, MiscCodec.instance);
                } else {
                    serializers.put(clazz, new JavaBeanSerializer(clazz, propertyNamingStrategy));
                }
            }

            writer = serializers.get(clazz);
        }
        return writer;
    }

    public boolean put(Type key, ObjectSerializer value) {
        return this.serializers.put(key, value);
    }
    
    /**
     * @since 1.1.52.android
     */
    public String getTypeKey() {
        return typeKey;
    }

    /**
     * @since 1.1.52.android
     */
    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }
}
