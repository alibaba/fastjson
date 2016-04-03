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
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import com.alibaba.fastjson.util.IdentityHashMap;

/**
 * circular references detect
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class SerializeConfig extends IdentityHashMap<Type, ObjectSerializer> {

    public final static SerializeConfig globalInstance = new SerializeConfig();

    public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
        return new JavaBeanSerializer(clazz);
    }

    public final static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    public SerializeConfig(){
        this(1024);
    }

    public SerializeConfig(int tableSize){
        super(tableSize);

        put(Boolean.class, BooleanCodec.instance);
        put(Character.class, MiscCodec.instance);
        put(Byte.class, IntegerCodec.instance);
        put(Short.class, IntegerCodec.instance);
        put(Integer.class, IntegerCodec.instance);
        put(Long.class, IntegerCodec.instance);
        put(Float.class, NumberCodec.instance);
        put(Double.class, NumberCodec.instance);
        put(BigDecimal.class, BigDecimalCodec.instance);
        put(BigInteger.class, BigDecimalCodec.instance);
        put(String.class, StringCodec.instance);
        put(Object[].class, ArrayCodec.instance);
        put(Class.class, MiscCodec.instance);

        put(SimpleDateFormat.class, MiscCodec.instance);
        put(Locale.class, MiscCodec.instance);
        put(Currency.class, MiscCodec.instance);
        put(TimeZone.class, MiscCodec.instance);
        put(UUID.class, MiscCodec.instance);
        put(URI.class, MiscCodec.instance);
        put(URL.class, MiscCodec.instance);
        put(Pattern.class, MiscCodec.instance);
        put(Charset.class, MiscCodec.instance);

    }

}
