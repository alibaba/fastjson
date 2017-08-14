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
package com.alibaba.fastjson.serializer;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.*;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;
import sun.reflect.annotation.AnnotationType;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * circular references detect
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class SerializeConfig {

    public final static SerializeConfig                   globalInstance  = new SerializeConfig();

    private static boolean                                awtError        = false;
    private static boolean                                jdk8Error       = false;
    private static boolean                                oracleJdbcError = false;
    private static boolean                                springfoxError  = false;
    private static boolean                                guavaError      = false;

    private boolean                                       asm             = !ASMUtils.IS_ANDROID;
    private ASMSerializerFactory                          asmFactory;
    protected String                                      typeKey         = JSON.DEFAULT_TYPE_KEY;
    public PropertyNamingStrategy                         propertyNamingStrategy;

    private final IdentityHashMap<Type, ObjectSerializer> serializers;

    private final boolean                                 fieldBased;
    
	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}
	
    private final JavaBeanSerializer createASMSerializer(SerializeBeanInfo beanInfo) throws Exception {
        JavaBeanSerializer serializer = asmFactory.createJavaBeanSerializer(beanInfo);
        
        for (int i = 0; i < serializer.sortedGetters.length; ++i) {
            FieldSerializer fieldDeser = serializer.sortedGetters[i];
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
            if (fieldClass.isEnum()) {
                ObjectSerializer fieldSer = this.getObjectWriter(fieldClass);
                if (!(fieldSer instanceof EnumSerializer)) {
                    serializer.writeDirect = false;
                }
            }
        }
     
        return serializer;
    }

    public final ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
	    SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, null, propertyNamingStrategy, fieldBased);
	    if (beanInfo.fields.length == 0 && Iterable.class.isAssignableFrom(clazz)) {
	        return MiscCodec.instance;
	    }

	    return createJavaBeanSerializer(beanInfo);
	}
	
	public ObjectSerializer createJavaBeanSerializer(SerializeBeanInfo beanInfo) {
	    JSONType jsonType = beanInfo.jsonType;
	    
	    if (jsonType != null) {
	        Class<?> serializerClass = jsonType.serializer();
	        if (serializerClass != Void.class) {
	            try {
                    Object seralizer = serializerClass.newInstance();
                    if (seralizer instanceof ObjectSerializer) {
                        return (ObjectSerializer) seralizer;
                    }
                } catch (Throwable e) {
                    // skip
                }
	        }
	        
	        if (jsonType.asm() == false) {
	            asm = false;
	        }

            for (SerializerFeature feature : jsonType.serialzeFeatures()) {
                if (SerializerFeature.WriteNonStringValueAsString == feature //
                        || SerializerFeature.WriteEnumUsingToString == feature //
                        || SerializerFeature.NotWriteDefaultValue == feature) {
                    asm = false;
                    break;
                }
            }
        }

	    Class<?> clazz = beanInfo.beanType;
		if (!Modifier.isPublic(beanInfo.beanType.getModifiers())) {
			return new JavaBeanSerializer(beanInfo);
		}

		boolean asm = this.asm && !fieldBased;

		if (asm && asmFactory.classLoader.isExternalClass(clazz)
				|| clazz == Serializable.class || clazz == Object.class) {
			asm = false;
		}

		if (asm && !ASMUtils.checkName(clazz.getSimpleName())) {
		    asm = false;
		}

		if (asm && beanInfo.beanType.isInterface()) {
		    asm = false;
        }
		
		if (asm) {
    		for(FieldInfo fieldInfo : beanInfo.fields){
                Field field = fieldInfo.field;
                if (field != null && !field.getType().equals(fieldInfo.fieldClass)) {
                    asm = false;
                    break;
                }

                Method method = fieldInfo.method;
                if (method != null && !method.getReturnType().equals(fieldInfo.fieldClass)) {
                    asm = false;
                    break;
                }

    			JSONField annotation = fieldInfo.getAnnotation();
    			
    			if (annotation == null) {
    			    continue;
    			}

    			String format = annotation.format();
    			if (format.length() != 0) {
    			    if (fieldInfo.fieldClass == String.class && "trim".equals(format)) {

                    } else {
                        asm = false;
                        break;
                    }
                }

                if ((!ASMUtils.checkName(annotation.name())) //
                        || annotation.jsonDirect()
                        || annotation.serializeUsing() != Void.class
                        || annotation.unwrapped()
                        ) {
    				asm = false;
    				break;
    			}

                for (SerializerFeature feature : annotation.serialzeFeatures()) {
                    if (SerializerFeature.WriteNonStringValueAsString == feature //
                            || SerializerFeature.WriteEnumUsingToString == feature //
                            || SerializerFeature.NotWriteDefaultValue == feature
                            || SerializerFeature.WriteClassName == feature) {
                        asm = false;
                        break;
                    }
                }

                if (TypeUtils.isAnnotationPresentOneToMany(method)) {
    			    asm = true;
    			    break;
                }
    		}
		}
		
		if (asm) {
			try {
                ObjectSerializer asmSerializer = createASMSerializer(beanInfo);
                if (asmSerializer != null) {
                    return asmSerializer;
                }
            } catch (ClassNotFoundException ex) {
			    // skip
			} catch (ClassFormatError e) {
			    // skip
			} catch (ClassCastException e) {
				// skip
			} catch (Throwable e) {
				throw new JSONException("create asm serializer error, class "
						+ clazz, e);
			}
		}

		return new JavaBeanSerializer(beanInfo);
	}

	public boolean isAsmEnable() {
		return asm;
	}

	public void setAsmEnable(boolean asmEnable) {
	    if (ASMUtils.IS_ANDROID) {
	        return;
	    }
		this.asm = asmEnable;
	}

	public static SerializeConfig getGlobalInstance() {
		return globalInstance;
	}

	public SerializeConfig() {
		this(IdentityHashMap.DEFAULT_SIZE);
	}

    public SerializeConfig(boolean fieldBase) {
	    this(IdentityHashMap.DEFAULT_SIZE, fieldBase);
    }

    public SerializeConfig(int tableSize) {
        this(tableSize, false);
    }

	public SerializeConfig(int tableSize, boolean fieldBase) {
	    this.fieldBased = fieldBase;
	    serializers = new IdentityHashMap<Type, ObjectSerializer>(tableSize);
		
		try {
		    if (asm) {
		        asmFactory = new ASMSerializerFactory();
		    }
		} catch (Throwable eror) {
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
		put(Currency.class, new MiscCodec());
		put(TimeZone.class, MiscCodec.instance);
		put(InetAddress.class, MiscCodec.instance);
		put(Inet4Address.class, MiscCodec.instance);
		put(Inet6Address.class, MiscCodec.instance);
		put(InetSocketAddress.class, MiscCodec.instance);
		put(File.class, MiscCodec.instance);
		put(Appendable.class, AppendableSerializer.instance);
		put(StringBuffer.class, AppendableSerializer.instance);
		put(StringBuilder.class, AppendableSerializer.instance);
		put(Charset.class, ToStringSerializer.instance);
		put(Pattern.class, ToStringSerializer.instance);
		put(Locale.class, ToStringSerializer.instance);
		put(URI.class, ToStringSerializer.instance);
		put(URL.class, ToStringSerializer.instance);
		put(UUID.class, ToStringSerializer.instance);

		// atomic
		put(AtomicBoolean.class, AtomicCodec.instance);
		put(AtomicInteger.class, AtomicCodec.instance);
		put(AtomicLong.class, AtomicCodec.instance);
		put(AtomicReference.class, ReferenceCodec.instance);
		put(AtomicIntegerArray.class, AtomicCodec.instance);
		put(AtomicLongArray.class, AtomicCodec.instance);
		
		put(WeakReference.class, ReferenceCodec.instance);
		put(SoftReference.class, ReferenceCodec.instance);

        put(LinkedList.class, CollectionCodec.instance);
	}
	
	/**
	 * add class level serialize filter
	 * @since 1.2.10
	 */
	public void addFilter(Class<?> clazz, SerializeFilter filter) {
	    ObjectSerializer serializer = getObjectWriter(clazz);
	    
	    if (serializer instanceof SerializeFilterable) {
	        SerializeFilterable filterable = (SerializeFilterable) serializer;
	        
	        if (this != SerializeConfig.globalInstance) {
	            if (filterable == MapSerializer.instance) {
	                MapSerializer newMapSer = new MapSerializer();
	                this.put(clazz, newMapSer);
	                newMapSer.addFilter(filter);
	                return;
	            }
	        }
	        
	        filterable.addFilter(filter);
	    }
	}
	
    /** class level serializer feature config
     * @since 1.2.12
     */
    public void config(Class<?> clazz, SerializerFeature feature, boolean value) {
        ObjectSerializer serializer = getObjectWriter(clazz, false);
        
        if (serializer == null) {
            SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, null, propertyNamingStrategy);
            
            if (value) {
                beanInfo.features |= feature.mask;
            } else {
                beanInfo.features &= ~feature.mask;
            }
            
            serializer = this.createJavaBeanSerializer(beanInfo);
            
            put(clazz, serializer);
            return;
        }

        if (serializer instanceof JavaBeanSerializer) {
            JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) serializer;
            SerializeBeanInfo beanInfo = javaBeanSerializer.beanInfo;
            
            int originalFeaturs = beanInfo.features;
            if (value) {
                beanInfo.features |= feature.mask;
            } else {
                beanInfo.features &= ~feature.mask;
            }
            
            if (originalFeaturs == beanInfo.features) {
                return;
            }
            
            Class<?> serializerClass = serializer.getClass();
            if (serializerClass != JavaBeanSerializer.class) {
                ObjectSerializer newSerializer = this.createJavaBeanSerializer(beanInfo);
                this.put(clazz, newSerializer);
            }
        }
    }
    
    public ObjectSerializer getObjectWriter(Class<?> clazz) {
        return getObjectWriter(clazz, true);
    }
	
	private ObjectSerializer getObjectWriter(Class<?> clazz, boolean create) {
        ObjectSerializer writer = serializers.get(clazz);

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

            writer = serializers.get(clazz);
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

                writer = serializers.get(clazz);
            }
        }
        
        if (writer == null) {
            String className = clazz.getName();

            if (Map.class.isAssignableFrom(clazz)) {
                put(clazz, MapSerializer.instance);
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
                JSONType jsonType = clazz.getAnnotation(JSONType.class);
                if (jsonType != null && jsonType.serializeEnumAsJavaBean()) {
                    put(clazz, createJavaBeanSerializer(clazz));
                } else {
                    put(clazz, EnumSerializer.instance);
                }
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
                put(clazz, new ArraySerializer(componentType, compObjectSerializer));
            } else if (Throwable.class.isAssignableFrom(clazz)) {
                SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(clazz, null, propertyNamingStrategy);
                beanInfo.features |= SerializerFeature.WriteClassName.mask;
                put(clazz, new JavaBeanSerializer(beanInfo));
            } else if (TimeZone.class.isAssignableFrom(clazz) || Map.Entry.class.isAssignableFrom(clazz)) {
                put(clazz, MiscCodec.instance);
            } else if (Appendable.class.isAssignableFrom(clazz)) {
                put(clazz, AppendableSerializer.instance);
            } else if (Charset.class.isAssignableFrom(clazz)) {
                put(clazz, ToStringSerializer.instance);
            } else if (Enumeration.class.isAssignableFrom(clazz)) {
                put(clazz, EnumerationSerializer.instance);
            } else if (Calendar.class.isAssignableFrom(clazz) //
                    || XMLGregorianCalendar.class.isAssignableFrom(clazz)) {
                put(clazz, CalendarCodec.instance);
            } else if (Clob.class.isAssignableFrom(clazz)) {
                put(clazz, ClobSeriliazer.instance);
            } else if (TypeUtils.isPath(clazz)) {
                put(clazz, ToStringSerializer.instance);
            } else if (Iterator.class.isAssignableFrom(clazz)) {
                put(clazz, MiscCodec.instance);
            } else {
                if (className.startsWith("java.awt.") //
                    && AwtCodec.support(clazz) //
                ) {
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
                    return  AwtCodec.instance;
                }
                
                // jdk8
                if ((!jdk8Error) //
                    && (className.startsWith("java.time.") //
                        || className.startsWith("java.util.Optional") //
                        || className.equals("java.util.concurrent.atomic.LongAdder")
                        || className.equals("java.util.concurrent.atomic.DoubleAdder")
                    )) {
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

                        put(Class.forName("java.util.concurrent.atomic.LongAdder"), AdderSerializer.instance);
                        put(Class.forName("java.util.concurrent.atomic.DoubleAdder"), AdderSerializer.instance);
                        
                        writer = serializers.get(clazz);
                        if (writer != null) {
                            return writer;
                        }
                    } catch (Throwable e) {
                        // skip
                        jdk8Error = true;
                    }
                }
                
                if ((!oracleJdbcError) //
                    && className.startsWith("oracle.sql.")) {
                    try {
                        put(Class.forName("oracle.sql.DATE"), DateCodec.instance);
                        put(Class.forName("oracle.sql.TIMESTAMP"), DateCodec.instance);
                        
                        writer = serializers.get(clazz);
                        if (writer != null) {
                            return writer;
                        }
                    } catch (Throwable e) {
                        // skip
                        oracleJdbcError = true;
                    }
                }
                
                if ((!springfoxError) //
                    && className.equals("springfox.documentation.spring.web.json.Json")) {
                    try {
                        put(Class.forName("springfox.documentation.spring.web.json.Json"), //
                            SwaggerJsonSerializer.instance);
                        
                        writer = serializers.get(clazz);
                        if (writer != null) {
                            return writer;
                        }
                    } catch (ClassNotFoundException e) {
                        // skip
                        springfoxError = true;
                    }
                }

                if ((!guavaError) //
                        && className.startsWith("com.google.common.collect.")) {
                    try {
                        put(Class.forName("com.google.common.collect.HashMultimap"), //
                                GuavaCodec.instance);
                        put(Class.forName("com.google.common.collect.LinkedListMultimap"), //
                                GuavaCodec.instance);
                        put(Class.forName("com.google.common.collect.ArrayListMultimap"), //
                                GuavaCodec.instance);
                        put(Class.forName("com.google.common.collect.TreeMultimap"), //
                                GuavaCodec.instance);

                        writer = serializers.get(clazz);
                        if (writer != null) {
                            return writer;
                        }
                    } catch (ClassNotFoundException e) {
                        // skip
                        guavaError = true;
                    }
                }

                if (className.equals("net.sf.json.JSONNull")) {
                    try {
                        put(Class.forName("net.sf.json.JSONNull"), //
                                MiscCodec.instance);
                    } catch (ClassNotFoundException e) {
                        // skip
                    }
                    writer = serializers.get(clazz);
                    if (writer != null) {
                        return writer;
                    }
                }

                Class[] interfaces = clazz.getInterfaces();
                if (interfaces.length == 1 && interfaces[0].isAnnotation()) {
                    return AnnotationSerializer.instance;
                }

                if (TypeUtils.isProxy(clazz)) {
                    Class<?> superClazz = clazz.getSuperclass();

                    ObjectSerializer superWriter = getObjectWriter(superClazz);
                    put(clazz, superWriter);
                    return superWriter;
                }

                if (Proxy.isProxyClass(clazz)) {
                    Class handlerClass = null;

                    if (interfaces.length == 2) {
                        handlerClass = interfaces[1];
                    } else {
                        for (Class proxiedInterface : interfaces) {
                            if (proxiedInterface.getName().startsWith("org.springframework.aop.")) {
                                continue;
                            }
                            if (handlerClass != null) {
                                handlerClass = null; // multi-matched
                                break;
                            }
                            handlerClass = proxiedInterface;
                        }
                    }

                    if (handlerClass != null) {
                        ObjectSerializer superWriter = getObjectWriter(handlerClass);
                        put(clazz, superWriter);
                        return superWriter;
                    }
                }

                if (create) {
                    put(clazz, createJavaBeanSerializer(clazz));
                }
            }

            writer = serializers.get(clazz);
        }
        return writer;
    }
	
	public final ObjectSerializer get(Type key) {
	    return this.serializers.get(key);
	}

    public boolean put(Object type, Object value) {
        return put((Type)type, (ObjectSerializer)value);
    }

	public boolean put(Type type, ObjectSerializer value) {
        return this.serializers.put(type, value);
	}

    /**
     * 1.2.24
     * @param enumClasses
     */
	public void configEnumAsJavaBean(Class<? extends Enum>... enumClasses) {
        for (Class<? extends Enum> enumClass : enumClasses) {
            put(enumClass, createJavaBeanSerializer(enumClass));
        }
    }

    /**
     * for spring config support
     * @param propertyNamingStrategy
     */
    public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        this.propertyNamingStrategy = propertyNamingStrategy;
    }
}
