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
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessControlException;
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
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.BooleanFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.IntegerFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.LongFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.serializer.AtomicCodec;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharArrayCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CharsetCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.CurrencyCodec;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.MapCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.alibaba.fastjson.serializer.ReferenceCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.ServiceLoader;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {
    public final static String DENY_PROPERTY = "fastjson.parser.deny";

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    private static ParserConfig                             global           = new ParserConfig();

    private final IdentityHashMap<Type, ObjectDeserializer> derializers      = new IdentityHashMap<Type, ObjectDeserializer>();

    private boolean                                         asmEnable        = !ASMUtils.IS_ANDROID;

    protected final SymbolTable                             symbolTable      = new SymbolTable(512);
    
    protected ClassLoader                                   defaultClassLoader;

    protected ASMDeserializerFactory                        asmFactory;
    
    private static boolean                                  awtError         = false;
    private static boolean                                  jdk8Error        = false;
    
    private String[]                                        denyList    = new String[] { "java.lang.Thread" };
    
    public ParserConfig() {
        this(null, null);
    }
    
    public ParserConfig(ClassLoader parentClassLoader){
        this(null, parentClassLoader);
    }
    
    public ParserConfig(ASMDeserializerFactory asmFactory){
        this(asmFactory, null);
    }

    private ParserConfig(ASMDeserializerFactory asmFactory, ClassLoader parentClassLoader){
        if (asmFactory == null && !ASMUtils.IS_ANDROID) {
            try {
                if (parentClassLoader == null) {
                    asmFactory = new ASMDeserializerFactory();    
                } else {
                    asmFactory = new ASMDeserializerFactory(parentClassLoader);
                }
            } catch (ExceptionInInitializerError error) {
                // skip
            } catch (AccessControlException error) {
                // skip
            } catch (NoClassDefFoundError error) {
                // skip
            }
        }
        
        this.asmFactory = asmFactory;
        
        if (asmFactory == null) {
            asmEnable = false;
        }
        
        derializers.put(SimpleDateFormat.class, MiscCodec.instance);
        derializers.put(java.sql.Timestamp.class, SqlDateDeserializer.instance_timestamp);
        derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
        derializers.put(java.sql.Time.class, TimeDeserializer.instance);
        derializers.put(java.util.Date.class, DateCodec.instance);
        derializers.put(Calendar.class, CalendarCodec.instance);

        derializers.put(JSONObject.class, MapCodec.instance);
        derializers.put(JSONArray.class, CollectionCodec.instance);

        derializers.put(Map.class, MapCodec.instance);
        derializers.put(HashMap.class, MapCodec.instance);
        derializers.put(LinkedHashMap.class, MapCodec.instance);
        derializers.put(TreeMap.class, MapCodec.instance);
        derializers.put(ConcurrentMap.class, MapCodec.instance);
        derializers.put(ConcurrentHashMap.class, MapCodec.instance);

        derializers.put(Collection.class, CollectionCodec.instance);
        derializers.put(List.class, CollectionCodec.instance);
        derializers.put(ArrayList.class, CollectionCodec.instance);

        derializers.put(Object.class, JavaObjectDeserializer.instance);
        derializers.put(String.class, StringCodec.instance);
        derializers.put(StringBuffer.class, StringCodec.instance);
        derializers.put(StringBuilder.class, StringCodec.instance);
        derializers.put(char.class, CharacterCodec.instance);
        derializers.put(Character.class, CharacterCodec.instance);
        derializers.put(byte.class, NumberDeserializer.instance);
        derializers.put(Byte.class, NumberDeserializer.instance);
        derializers.put(short.class, NumberDeserializer.instance);
        derializers.put(Short.class, NumberDeserializer.instance);
        derializers.put(int.class, IntegerCodec.instance);
        derializers.put(Integer.class, IntegerCodec.instance);
        derializers.put(long.class, LongCodec.instance);
        derializers.put(Long.class, LongCodec.instance);
        derializers.put(BigInteger.class, BigIntegerCodec.instance);
        derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        derializers.put(float.class, FloatCodec.instance);
        derializers.put(Float.class, FloatCodec.instance);
        derializers.put(double.class, NumberDeserializer.instance);
        derializers.put(Double.class, NumberDeserializer.instance);
        derializers.put(boolean.class, BooleanCodec.instance);
        derializers.put(Boolean.class, BooleanCodec.instance);
        derializers.put(Class.class, MiscCodec.instance);
        derializers.put(char[].class, CharArrayCodec.instance);

        derializers.put(AtomicBoolean.class, BooleanCodec.instance);
        derializers.put(AtomicInteger.class, IntegerCodec.instance);
        derializers.put(AtomicLong.class, LongCodec.instance);
        derializers.put(AtomicReference.class, ReferenceCodec.instance);

        derializers.put(WeakReference.class, ReferenceCodec.instance);
        derializers.put(SoftReference.class, ReferenceCodec.instance);

        derializers.put(UUID.class, MiscCodec.instance);
        derializers.put(TimeZone.class, MiscCodec.instance);
        derializers.put(Locale.class, MiscCodec.instance);
        derializers.put(Currency.class, CurrencyCodec.instance);
        derializers.put(InetAddress.class, MiscCodec.instance);
        derializers.put(Inet4Address.class, MiscCodec.instance);
        derializers.put(Inet6Address.class, MiscCodec.instance);
        derializers.put(InetSocketAddress.class, MiscCodec.instance);
        derializers.put(File.class, MiscCodec.instance);
        derializers.put(URI.class, MiscCodec.instance);
        derializers.put(URL.class, MiscCodec.instance);
        derializers.put(Pattern.class, MiscCodec.instance);
        derializers.put(Charset.class, CharsetCodec.instance);
        derializers.put(Number.class, NumberDeserializer.instance);
        derializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        derializers.put(AtomicLongArray.class, AtomicCodec.instance);
        derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);

        derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        derializers.put(Closeable.class, JavaObjectDeserializer.instance);

        if (!awtError) {
            try {
                derializers.put(Class.forName("java.awt.Point"), AwtCodec.instance);
                derializers.put(Class.forName("java.awt.Font"), AwtCodec.instance);
                derializers.put(Class.forName("java.awt.Rectangle"), AwtCodec.instance);
                derializers.put(Class.forName("java.awt.Color"), AwtCodec.instance);
            } catch (Throwable e) {
                // skip
                awtError = true;
            }
        }
        
        if (!jdk8Error) {
            try {
                derializers.put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.ZoneId"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
                derializers.put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
                
                derializers.put(Class.forName("java.util.Optional"), OptionalCodec.instance);
                derializers.put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
                derializers.put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
                derializers.put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
            } catch (Throwable e) {
                // skip
                jdk8Error = true;
            }
        }
        
        addDeny("java.lang.Thread");
        configFromPropety(System.getProperties());
    }
    
    public void configFromPropety(Properties properties) {
        String property = properties.getProperty(DENY_PROPERTY);
        if (property != null && property.length() > 0) {
            String[] items = property.split(",");
            for (int i = 0; i < items.length; ++i) {
                String item = items[i];
                this.addDeny(item);
            }
        }
    }

    public boolean isAsmEnable() {
        return asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return derializers;
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

        return JavaObjectDeserializer.instance;
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (type == null) {
            type = clazz;
        }

        derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        {
            JSONType annotation = clazz.getAnnotation(JSONType.class);
            if (annotation != null) {
                Class<?> mappingTo = annotation.mappingTo();
                if (mappingTo != Void.class) {
                    return getDeserializer(mappingTo, mappingTo);
                }
            }
        }

        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof ParameterizedType) {
            derializer = derializers.get(clazz);
        }

        if (derializer != null) {
            return derializer;
        }
        
        for (int i = 0; i < denyList.length; ++i) {
            String deny = denyList[i];
            String className = clazz.getName();
            className = className.replace('$', '.');
            if (className.startsWith(deny)) {
                throw new JSONException("parser deny : " + className);
            }
        }

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            for (AutowiredObjectDeserializer autowired : ServiceLoader.load(AutowiredObjectDeserializer.class,
                                                                            classLoader)) {
                for (Type forType : autowired.getAutowiredFor()) {
                    derializers.put(forType, autowired);
                }
            }
        } catch (Exception ex) {
            // skip
        }

        derializer = derializers.get(type);
        if (derializer != null) {
            return derializer;
        }

        if (clazz.isEnum()) {
            derializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            derializer = ObjectArrayCodec.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class
                   || clazz == ArrayList.class) {
            derializer = CollectionCodec.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            derializer = CollectionCodec.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            derializer = MapCodec.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }

        putDeserializer(type, derializer);

        return derializer;
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        boolean asmEnable = this.asmEnable;
        if (asmEnable) {
            Class<?> superClass = JavaBeanInfo.getBuilderClass(clazz.getAnnotation(JSONType.class));
            if (superClass == null) {
                superClass = clazz;
            }

            for (;;) {
                if (!Modifier.isPublic(superClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                superClass = superClass.getSuperclass();
                if (superClass == Object.class || superClass == null) {
                    break;
                }
            }
        }

        if (clazz.getTypeParameters().length != 0) {
            asmEnable = false;
        }

        if (asmEnable && asmFactory != null && asmFactory.isExternalClass(clazz)) {
            asmEnable = false;
        }
        
        if (asmEnable) {
            asmEnable = ASMUtils.checkName(clazz.getName());
        }

        if (asmEnable) {
            if (clazz.isInterface()) {
                asmEnable = false;
            }
            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type);
            
            
            if (asmEnable && beanInfo.fields.length > 200) {
                asmEnable = false;
            }

            Constructor<?> defaultConstructor = beanInfo.defaultConstructor;
            if (asmEnable && defaultConstructor == null && !clazz.isInterface()) {
                asmEnable = false;
            }

            for (FieldInfo fieldInfo : beanInfo.fields) {
                if (fieldInfo.getOnly) {
                    asmEnable = false;
                    break;
                }

                Class<?> fieldClass = fieldInfo.fieldClass;
                if (!Modifier.isPublic(fieldClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isMemberClass() && !Modifier.isStatic(fieldClass.getModifiers())) {
                    asmEnable = false;
                }
                
                if (!ASMUtils.checkName(fieldInfo.getMember().getName())) {
                    asmEnable = false;
                }
                
                JSONField annotation = fieldInfo.getAnnotation();
                if (annotation != null && !ASMUtils.checkName(annotation.name())) {
                	asmEnable = false;
				}
            }
        }

        if (asmEnable) {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                asmEnable = false;
            }
        }
        
        if (!asmEnable) {
            return new JavaBeanDeserializer(this, clazz, type);
        }

        try {
            return asmFactory.createJavaBeanDeserializer(this, clazz, type);
            // } catch (VerifyError e) {
            // e.printStackTrace();
            // return new JavaBeanDeserializer(this, clazz, type);
        } catch (NoSuchMethodException ex) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (JSONException asmError) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (Exception e) {
            throw new JSONException("create asm deserializer error, " + clazz.getName(), e);
        }
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, JavaBeanInfo beanInfo, FieldInfo fieldInfo) {
        boolean asmEnable = this.asmEnable;

        Class<?> clazz = beanInfo.clazz;
        if (asmEnable) {
            Class<?> superClass = beanInfo.builderClass;
            if (superClass == null) {
                superClass = clazz;
            }

            for (;;) {
                if (!Modifier.isPublic(superClass.getModifiers())) {
                    asmEnable = false;
                    break;
                }

                superClass = superClass.getSuperclass();
                if (superClass == Object.class || superClass == null) {
                    break;
                }
            }
        }
        
        if (fieldInfo.fieldClass == Class.class) {
            asmEnable = false;
        }

        if (asmEnable && asmFactory != null && asmFactory.isExternalClass(clazz)) {
            asmEnable = false;
        }

        if (!asmEnable) {
            return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
        }

        try {
            return asmFactory.createFieldDeserializer(mapping, clazz, fieldInfo);
        } catch (Throwable e) {
            // skip
        }

        return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
    }

    public FieldDeserializer createFieldDeserializerWithoutASM(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.fieldClass;

        if (fieldClass == boolean.class || fieldClass == Boolean.class) {
            return new BooleanFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == int.class || fieldClass == Integer.class) {
            return new IntegerFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == long.class || fieldClass == Long.class) {
            return new LongFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == String.class) {
            return new StringFieldDeserializer(mapping, clazz, fieldInfo);
        }

        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        derializers.put(type, deserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    public boolean isPrimitive(Class<?> clazz) {
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

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = getField0(clazz, fieldName);
        if (field == null) {
            field = getField0(clazz, "_" + fieldName);
        }
        if (field == null) {
            field = getField0(clazz, "m_" + fieldName);
        }
        return field;
    }

    private static Field getField0(Class<?> clazz, String fieldName) {
        for (Field item : clazz.getDeclaredFields()) {
            if (fieldName.equals(item.getName())) {
                return item;
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            return getField(clazz.getSuperclass(), fieldName);
        }

        return null;
    }

    public ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }
    
    public void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        this.defaultClassLoader = defaultClassLoader;
    }
    
    public void addDeny(String name) {
        if (name == null || name.length() == 0) {
            return;
        }
        
        String[] denyList = new String[this.denyList.length + 1];
        System.arraycopy(this.denyList, 0, denyList, 0, this.denyList.length);
        denyList[denyList.length - 1] = name;
        this.denyList = denyList;
    }
}
