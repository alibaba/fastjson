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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
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
import java.sql.Clob;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * circular references detect
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class SerializeConfig extends IdentityHashMap<Type, ObjectSerializer> {
	private final static SerializeConfig globalInstance = new SerializeConfig();

	private static boolean awtError = false;
	private static boolean jdk8Error = false;
	private static boolean oracleJdbcError = false;
	
	private boolean asm = !ASMUtils.IS_ANDROID;

	private ASMSerializerFactory asmFactory;
	

	private String typeKey = JSON.DEFAULT_TYPE_KEY;
	
	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public final ObjectSerializer createASMSerializer(Class<?> clazz)
			throws Exception {
		return asmFactory.createJavaBeanSerializer(clazz, null);
	}
	
	public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
		if (!Modifier.isPublic(clazz.getModifiers())) {
			return new JavaBeanSerializer(clazz);
		}

		boolean asm = this.asm;

		if (asm && asmFactory.classLoader.isExternalClass(clazz)
				|| clazz == Serializable.class || clazz == Object.class) {
			asm = false;
		}

		{
			JSONType annotation = clazz.getAnnotation(JSONType.class);
			if (annotation != null && annotation.asm() == false) {
				asm = false;
			}
		}

		if (asm && !ASMUtils.checkName(clazz.getName())) {
		    asm = false;
		}
		
		if (asm) {
    		for(Field field : clazz.getDeclaredFields()){
    			JSONField annotation = field.getAnnotation(JSONField.class);
    			if (annotation != null && !ASMUtils.checkName(annotation.name())) {
    				asm = false;
    				break;
    			}
    		}
		}
		
		if (asm) {
			try {
			    ObjectSerializer asmSerializer = createASMSerializer(clazz);
			    if (asmSerializer != null) {
			        return asmSerializer;
			    }
			} catch (ClassCastException e) {
				// skip
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

	public static SerializeConfig getGlobalInstance() {
		return globalInstance;
	}

	public SerializeConfig() {
		this(1024);
	}

	public SerializeConfig(int tableSize) {
		super(tableSize);
		
		try {
		    if (asm) {
		        asmFactory = new ASMSerializerFactory();
		    }
		} catch (NoClassDefFoundError eror) {
		    asm = false;
		} catch (ExceptionInInitializerError error) {
		    asm = false;
		}

		put(Boolean.class, BooleanCodec.instance);
		put(Character.class, CharacterCodec.instance);
		put(Byte.class, IntegerCodec.instance);
		put(Short.class, IntegerCodec.instance);
		put(Integer.class, IntegerCodec.instance);
		put(Long.class, LongCodec.instance);
		put(Float.class, FloatCodec.instance);
		put(Double.class, DoubleSerializer.instance);
		put(BigDecimal.class, BigDecimalCodec.instance);
		put(BigInteger.class, BigIntegerCodec.instance);
		put(String.class, StringCodec.instance);
		put(byte[].class, PrimitiveArraySerializer.instance);
		put(short[].class, PrimitiveArraySerializer.instance);
		put(int[].class, PrimitiveArraySerializer.instance);
		put(long[].class, PrimitiveArraySerializer.instance);
		put(float[].class, PrimitiveArraySerializer.instance);
		put(double[].class, PrimitiveArraySerializer.instance);
		put(boolean[].class, PrimitiveArraySerializer.instance);
		put(char[].class, PrimitiveArraySerializer.instance);
		put(Object[].class, ObjectArrayCodec.instance);
		put(Class.class, MiscCodec.instance);

		put(SimpleDateFormat.class, MiscCodec.instance);
		put(Locale.class, MiscCodec.instance);
		put(Currency.class, CurrencyCodec.instance);
		put(TimeZone.class, MiscCodec.instance);
		put(UUID.class, MiscCodec.instance);
		put(InetAddress.class, MiscCodec.instance);
		put(Inet4Address.class, MiscCodec.instance);
		put(Inet6Address.class, MiscCodec.instance);
		put(InetSocketAddress.class, MiscCodec.instance);
		put(File.class, MiscCodec.instance);
		put(URI.class, MiscCodec.instance);
		put(URL.class, MiscCodec.instance);
		put(Appendable.class, AppendableSerializer.instance);
		put(StringBuffer.class, AppendableSerializer.instance);
		put(StringBuilder.class, AppendableSerializer.instance);
		put(Pattern.class, MiscCodec.instance);
		put(Charset.class, CharsetCodec.instance);

		// atomic
		put(AtomicBoolean.class, AtomicCodec.instance);
		put(AtomicInteger.class, AtomicCodec.instance);
		put(AtomicLong.class, AtomicCodec.instance);
		put(AtomicReference.class, ReferenceCodec.instance);
		put(AtomicIntegerArray.class, AtomicCodec.instance);
		put(AtomicLongArray.class, AtomicCodec.instance);
		
		put(WeakReference.class, ReferenceCodec.instance);
		put(SoftReference.class, ReferenceCodec.instance);

		// awt
		if (!awtError) {
    		try {
    			put(Class.forName("java.awt.Color"), AwtCodec.instance);
    			put(Class.forName("java.awt.Font"), AwtCodec.instance);
    			put(Class.forName("java.awt.Point"), AwtCodec.instance);
                put(Class.forName("java.awt.Rectangle"), AwtCodec.instance);
    		} catch (Throwable e) {
    		    awtError = true;
    			// skip
    		}
		}
		
		// jdk8
		if (!jdk8Error) {
    		try {
    		    put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
    		    put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
    		    
    		    put(Class.forName("java.util.Optional"), OptionalCodec.instance);
    		    put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
    		    put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
    		    put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
    		} catch (Throwable e) {
    		    // skip
    		    jdk8Error = true;
    		}
		}
		
		if (!oracleJdbcError) {
		    try {
                put(Class.forName("oracle.sql.DATE"), DateCodec.instance);
                put(Class.forName("oracle.sql.TIMESTAMP"), DateCodec.instance);
            } catch (Throwable e) {
                // skip
                oracleJdbcError = true;
            }
		}
	}

	public ObjectSerializer getObjectWriter(Class<?> clazz) {
        ObjectSerializer writer = get(clazz);

        if (writer == null) {
            try {
                final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                for (Object o : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {
                    if (!(o instanceof AutowiredObjectSerializer)) {
                        continue;
                    }

                    AutowiredObjectSerializer autowired = (AutowiredObjectSerializer) o;
                    for (Type forType : autowired.getAutowiredFor()) {
                        put(forType, autowired);
                    }
                }
            } catch (ClassCastException ex) {
                // skip
            }

            writer = get(clazz);
        }

        if (writer == null) {
            final ClassLoader classLoader = JSON.class.getClassLoader();
            if (classLoader != Thread.currentThread().getContextClassLoader()) {
                try {
                    for (Object o : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {

                        if (!(o instanceof AutowiredObjectSerializer)) {
                            continue;
                        }

                        AutowiredObjectSerializer autowired = (AutowiredObjectSerializer) o;
                        for (Type forType : autowired.getAutowiredFor()) {
                            put(forType, autowired);
                        }
                    }
                } catch (ClassCastException ex) {
                    // skip
                }

                writer = get(clazz);
            }
        }

        if (writer == null) {
            if (Map.class.isAssignableFrom(clazz)) {
                put(clazz, MapCodec.instance);
            } else if (List.class.isAssignableFrom(clazz)) {
                put(clazz, ListSerializer.instance);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                put(clazz, CollectionCodec.instance);
            } else if (Date.class.isAssignableFrom(clazz)) {
                put(clazz, DateCodec.instance);
            } else if (JSONAware.class.isAssignableFrom(clazz)) {
                put(clazz, JSONAwareSerializer.instance);
            } else if (JSONSerializable.class.isAssignableFrom(clazz)) {
                put(clazz, JSONSerializableSerializer.instance);
            } else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
                put(clazz, MiscCodec.instance);
            } else if (clazz.isEnum() || (clazz.getSuperclass() != null && clazz.getSuperclass().isEnum())) {
                put(clazz, EnumSerializer.instance);
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
                put(clazz, new ArraySerializer(componentType, compObjectSerializer));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                int features = TypeUtils.getSerializeFeatures(clazz);
                features |= SerializerFeature.WriteClassName.mask;
                put(clazz, new JavaBeanSerializer(clazz, null, features));
            } else if (TimeZone.class.isAssignableFrom(clazz)) {
                put(clazz, MiscCodec.instance);
            } else if (Appendable.class.isAssignableFrom(clazz)) {
                put(clazz, AppendableSerializer.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                put(clazz, CharsetCodec.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                put(clazz, EnumerationSerializer.instance);
            } else if (Calendar.class.isAssignableFrom(clazz)) {
                put(clazz, CalendarCodec.instance);
            } else if (Clob.class.isAssignableFrom(clazz)) {
                put(clazz, ClobSeriliazer.instance);
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

                    ObjectSerializer superWriter = getObjectWriter(superClazz);
                    put(clazz, superWriter);
                    return superWriter;
                }

                if (Proxy.isProxyClass(clazz)) {
                    put(clazz, createJavaBeanSerializer(clazz));
                } else {
                    put(clazz, createJavaBeanSerializer(clazz));
                }
            }

            writer = get(clazz);
        }
        return writer;
    }
}
