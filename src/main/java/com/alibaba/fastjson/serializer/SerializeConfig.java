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
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
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
import java.util.Currency;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IdentityHashMap;

/**
 * circular references detect
 * 
 * @author wenshao<szujobs@hotmail.com>
 */
public class SerializeConfig extends IdentityHashMap<Type, ObjectSerializer> {
	private final static SerializeConfig globalInstance = new SerializeConfig();

	private boolean asm = !ASMUtils.isAndroid();;

	private final ASMSerializerFactory asmFactory = new ASMSerializerFactory();
	

	private String typeKey = JSON.DEFAULT_TYPE_KEY;
	
	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public final ObjectSerializer createASMSerializer(Class<?> clazz)
			throws Exception {
		return asmFactory.createJavaBeanSerializer(clazz);
	}

	public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
		if (!Modifier.isPublic(clazz.getModifiers())) {
			return new JavaBeanSerializer(clazz);
		}

		boolean asm = this.asm;

		if (asm && asmFactory.isExternalClass(clazz)
				|| clazz == Serializable.class || clazz == Object.class) {
			asm = false;
		}

		{
			JSONType annotation = clazz.getAnnotation(JSONType.class);
			if (annotation != null && annotation.asm() == false) {
				asm = false;
			}
		}

		if (asm) {
			try {
				return createASMSerializer(clazz);
			} catch (ClassCastException e) {
				// skip
				return new JavaBeanSerializer(clazz);
			} catch (Throwable e) {
				throw new JSONException("create asm serializer error, class "
						+ clazz, e);
			}
		}

		return new JavaBeanSerializer(clazz);
	}

	public boolean isAsmEnable() {
		return asm;
	}

	public void setAsmEnable(boolean asmEnable) {
		this.asm = asmEnable;
	}

	public final static SerializeConfig getGlobalInstance() {
		return globalInstance;
	}

	public SerializeConfig() {
		this(DEFAULT_TABLE_SIZE);
	}

	public SerializeConfig(int tableSize) {
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
		put(Locale.class, LocaleSerializer.instance);
		put(Currency.class, CurrencyCodec.instance);
		put(TimeZone.class, TimeZoneSerializer.instance);
		put(UUID.class, UUIDSerializer.instance);
		put(InetAddress.class, InetAddressSerializer.instance);
		put(Inet4Address.class, InetAddressSerializer.instance);
		put(Inet6Address.class, InetAddressSerializer.instance);
		put(InetSocketAddress.class, InetSocketAddressSerializer.instance);
		put(File.class, FileSerializer.instance);
		put(URI.class, URISerializer.instance);
		put(URL.class, URLSerializer.instance);
		put(Appendable.class, AppendableSerializer.instance);
		put(StringBuffer.class, AppendableSerializer.instance);
		put(StringBuilder.class, AppendableSerializer.instance);
		put(Pattern.class, PatternSerializer.instance);
		put(Charset.class, CharsetSerializer.instance);

		// atomic
		put(AtomicBoolean.class, AtomicBooleanSerializer.instance);
		put(AtomicInteger.class, AtomicIntegerSerializer.instance);
		put(AtomicLong.class, AtomicLongSerializer.instance);
		put(AtomicReference.class, ReferenceSerializer.instance);
		put(AtomicIntegerArray.class, AtomicIntegerArraySerializer.instance);
		put(AtomicLongArray.class, AtomicLongArraySerializer.instance);
		
		put(WeakReference.class, ReferenceSerializer.instance);
		put(SoftReference.class, ReferenceSerializer.instance);

		// awt
		try {
			put(Class.forName("java.awt.Color"), ColorSerializer.instance);
			put(Class.forName("java.awt.Font"), FontSerializer.instance);
			put(Class.forName("java.awt.Point"), PointSerializer.instance);
			put(Class.forName("java.awt.Rectangle"),
					RectangleSerializer.instance);
		} catch (Throwable e) {
			// skip
		}

	}

}
