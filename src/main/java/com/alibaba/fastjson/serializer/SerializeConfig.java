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

import java.io.File;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import com.alibaba.fastjson.util.IdentityHashMap;

/**
 * circular references detect
 * 
 * @author wenshao<szujobs@hotmail.com>
 */
public class SerializeConfig extends IdentityHashMap<Type, ObjectSerializer> {

    private final static SerializeConfig globalInstance = new SerializeConfig();

    public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
        return new JavaBeanSerializer(clazz);
    }

    public final static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    public SerializeConfig(){
        this(DEFAULT_TABLE_SIZE);
    }

    public SerializeConfig(int tableSize){
        super(tableSize);

        put(Boolean.class, BooleanSerializer.instance);
        put(Character.class, CharacterSerializer.instance);
        put(Byte.class, ByteSerializer.instance);
        put(Short.class, ShortSerializer.instance);
        put(Integer.class, IntegerSerializer.instance);
        put(Long.class, LongSerializer.instance);
        put(Float.class, FloatSerializer.instance);
        put(Double.class, DoubleSerializer.instance);
        put(BigDecimal.class, BigDecimalSerializer.instance);
        put(BigInteger.class, BigIntegerSerializer.instance);
        put(String.class, StringSerializer.instance);
        put(byte[].class, ByteArraySerializer.instance);
        put(short[].class, ShortArraySerializer.instance);
        put(int[].class, IntArraySerializer.instance);
        put(long[].class, LongArraySerializer.instance);
        put(float[].class, FloatArraySerializer.instance);
        put(double[].class, DoubleArraySerializer.instance);
        put(boolean[].class, BooleanArraySerializer.instance);
        put(char[].class, CharArraySerializer.instance);
        put(Object[].class, ObjectArraySerializer.instance);
        put(Class.class, ClassSerializer.instance);

        put(SimpleDateFormat.class, DateFormatSerializer.instance);
        put(Locale.class, ToStringSerializer.instance);
        put(TimeZone.class, TimeZoneSerializer.instance);
        put(UUID.class, ToStringSerializer.instance);
        put(InetAddress.class, InetAddressSerializer.instance);
        put(Inet4Address.class, InetAddressSerializer.instance);
        put(Inet6Address.class, InetAddressSerializer.instance);
        put(InetSocketAddress.class, InetSocketAddressSerializer.instance);
        put(File.class, FileSerializer.instance);
        put(URI.class, ToStringSerializer.instance);
        put(URL.class, ToStringSerializer.instance);
        put(Appendable.class, AppendableSerializer.instance);
        put(StringBuffer.class, AppendableSerializer.instance);
        put(StringBuilder.class, AppendableSerializer.instance);
        put(StringWriter.class, AppendableSerializer.instance);
        put(Pattern.class, PatternSerializer.instance);
        put(Charset.class, ToStringSerializer.instance);

        // atomic
        put(AtomicBoolean.class, AtomicBooleanSerializer.instance);
        put(AtomicInteger.class, AtomicIntegerSerializer.instance);
        put(AtomicLong.class, AtomicLongSerializer.instance);
        put(AtomicReference.class, ReferenceSerializer.instance);
        put(AtomicIntegerArray.class, AtomicIntegerArraySerializer.instance);
        put(AtomicLongArray.class, AtomicLongArraySerializer.instance);

        put(WeakReference.class, ReferenceSerializer.instance);
        put(SoftReference.class, ReferenceSerializer.instance);

    }

}
