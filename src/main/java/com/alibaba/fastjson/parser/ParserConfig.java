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
package com.alibaba.fastjson.parser;

import java.io.*;
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
import java.security.AccessControlException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
import com.alibaba.fastjson.asm.ClassReader;
import com.alibaba.fastjson.asm.TypeCollector;
import com.alibaba.fastjson.parser.deserializer.*;
import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.support.moneta.MonetaCodec;
import com.alibaba.fastjson.util.*;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.ServiceLoader;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {

    public static final String    DENY_PROPERTY             = "fastjson.parser.deny";
    public static final String    AUTOTYPE_ACCEPT           = "fastjson.parser.autoTypeAccept";
    public static final String    AUTOTYPE_SUPPORT_PROPERTY = "fastjson.parser.autoTypeSupport";

    public static  final String[] DENYS;
    private static final String[] AUTO_TYPE_ACCEPT_LIST;
    public static  final boolean  AUTO_SUPPORT;

    static  {
        {
            String property = IOUtils.getStringProperty(DENY_PROPERTY);
            DENYS = splitItemsFormProperty(property);
        }
        {
            String property = IOUtils.getStringProperty(AUTOTYPE_SUPPORT_PROPERTY);
            AUTO_SUPPORT = "true".equals(property);
        }
        {
            String property = IOUtils.getStringProperty(AUTOTYPE_ACCEPT);
            String[] items = splitItemsFormProperty(property);
            if (items == null) {
                items = new String[0];
            }
            AUTO_TYPE_ACCEPT_LIST = items;
        }
    }

    public static ParserConfig getGlobalInstance() {
        return global;
    }
    public static ParserConfig                              global                = new ParserConfig();

    private final IdentityHashMap<Type, ObjectDeserializer> deserializers         = new IdentityHashMap<Type, ObjectDeserializer>();
    private final IdentityHashMap<Type, IdentityHashMap<Type, ObjectDeserializer>> mixInDeserializers = new IdentityHashMap<Type, IdentityHashMap<Type, ObjectDeserializer>>(16);
    private final ConcurrentMap<String,Class<?>>            typeMapping           = new ConcurrentHashMap<String,Class<?>>(16, 0.75f, 1);

    private boolean                                         asmEnable             = !ASMUtils.IS_ANDROID;

    public final SymbolTable                                symbolTable           = new SymbolTable(4096);

    public PropertyNamingStrategy                           propertyNamingStrategy;

    protected ClassLoader                                   defaultClassLoader;

    protected ASMDeserializerFactory                        asmFactory;

    private static boolean                                  awtError              = false;
    private static boolean                                  jdk8Error             = false;
    private static boolean                                  jodaError             = false;
    private static boolean                                  guavaError            = false;

    private boolean                                         autoTypeSupport       = AUTO_SUPPORT;
    private long[]                                          denyHashCodes;
    private long[]                                          acceptHashCodes;


    public final boolean                                    fieldBased;
    private boolean                                         jacksonCompatible     = false;

    public boolean                                          compatibleWithJavaBean = TypeUtils.compatibleWithJavaBean;
    private List<Module>                                    modules                = new ArrayList<Module>();

    {
        denyHashCodes = new long[]{
                0x86fc2bf9beaf7aefL,
                0x8eadd40cb2a94443L,
                0x8f75f9fa0df03f80L,
                0x8fd1960988bce8b4L,
                0x9172a53f157930afL,
                0x92122d710e364fb8L,
                0x94305c26580f73c5L,
                0x9437792831df7d3fL,
                0xa123a62f93178b20L,
                0xaa3daffdb10c4937L,
                0xb7e8ed757f5d13a2L,
                0xbcdd9dc12766f0ceL,
                0xc2eb1e621f439309L,
                0xc7599ebfe3e72406L,
                0xc963695082fd728eL,
                0xd9c9dbf6bbd27bb1L,
                0xdf2ddff310cdb375L,
                0xe09ae4604842582fL,
                0xe603d6a51fad692bL,
                0xe9184be55b1d962aL,
                0xe9f20bad25f60807L,
                0xeea210e8da2ec6e1L,
                0xfc773ae20c827691L,
                0xfd5bfc610056d720L,
                0xffdd1a80f1ed3405L,
                0x761619136cc13eL,
                0x1603dc147a3e358L,
                0x45b11bc78a3aba3L,
                0xee6511b66fd5ef0L,
                0x10b2bdca849d9b3eL,
                0x144277b467723158L,
                0x14db2e6fead04af0L,
                0x2b3a37467a344cdfL,
                0x313bb4abd8d4554cL,
                0x332f0b5369a18310L,
                0x33c64b921f523f2fL,
                0x34a81ee78429fdf1L,
                0x398f942e01920cf0L,
                0x42d11a560fc9fba9L,
                0x440e89208f445fb9L,
                0x46c808a4b5841f57L,
                0x4a3797b30328202cL,
                0x4ba3e254e758d70dL,
                0x4ef08c90ff16c675L,
                0x4fd10ddc6d13821fL,
                0x527db6b46ce3bcbcL,
                0x599b5c1213a099acL,
                0x5a5bd85c072e5efeL,
                0x5d92e6ddde40ed84L,
                0x616323f12c2ce25eL,
                0x63a220e60a17c7b9L,
                0x6749835432e0f0d2L,
                0x746bd4a53ec195fbL,
                0x74b50bb9260e31ffL,
                0x767a586a5107feefL,
                0x7aa7ee3627a19cf3L,
                0x7bddd363ad3998c6L
        };

        long[] hashCodes = new long[AUTO_TYPE_ACCEPT_LIST.length + 1];
        for (int i = 0; i < AUTO_TYPE_ACCEPT_LIST.length; i++) {
            hashCodes[i] = TypeUtils.fnv1a_64(AUTO_TYPE_ACCEPT_LIST[i]);
        }
        hashCodes[hashCodes.length - 1] = -6293031534589903644L;

        Arrays.sort(hashCodes);
        acceptHashCodes = hashCodes;
    }

    public ParserConfig(){
        this(false);
    }

    public ParserConfig(boolean fieldBase){
        this(null, null, fieldBase);
    }

    public ParserConfig(ClassLoader parentClassLoader){
        this(null, parentClassLoader, false);
    }

    public ParserConfig(ASMDeserializerFactory asmFactory){
        this(asmFactory, null, false);
    }

    private ParserConfig(ASMDeserializerFactory asmFactory, ClassLoader parentClassLoader, boolean fieldBased){
        this.fieldBased = fieldBased;
        if (asmFactory == null && !ASMUtils.IS_ANDROID) {
            try {
                if (parentClassLoader == null) {
                    asmFactory = new ASMDeserializerFactory(new ASMClassLoader());
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

        initDeserializers();

        addItemsToDeny(DENYS);
        addItemsToAccept(AUTO_TYPE_ACCEPT_LIST);

    }

    private void initDeserializers() {
        deserializers.put(SimpleDateFormat.class, MiscCodec.instance);
        deserializers.put(java.sql.Timestamp.class, SqlDateDeserializer.instance_timestamp);
        deserializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
        deserializers.put(java.sql.Time.class, TimeDeserializer.instance);
        deserializers.put(java.util.Date.class, DateCodec.instance);
        deserializers.put(Calendar.class, CalendarCodec.instance);
        deserializers.put(XMLGregorianCalendar.class, CalendarCodec.instance);

        deserializers.put(JSONObject.class, MapDeserializer.instance);
        deserializers.put(JSONArray.class, CollectionCodec.instance);

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
        deserializers.put(StringBuffer.class, StringCodec.instance);
        deserializers.put(StringBuilder.class, StringCodec.instance);
        deserializers.put(char.class, CharacterCodec.instance);
        deserializers.put(Character.class, CharacterCodec.instance);
        deserializers.put(byte.class, NumberDeserializer.instance);
        deserializers.put(Byte.class, NumberDeserializer.instance);
        deserializers.put(short.class, NumberDeserializer.instance);
        deserializers.put(Short.class, NumberDeserializer.instance);
        deserializers.put(int.class, IntegerCodec.instance);
        deserializers.put(Integer.class, IntegerCodec.instance);
        deserializers.put(long.class, LongCodec.instance);
        deserializers.put(Long.class, LongCodec.instance);
        deserializers.put(BigInteger.class, BigIntegerCodec.instance);
        deserializers.put(BigDecimal.class, BigDecimalCodec.instance);
        deserializers.put(float.class, FloatCodec.instance);
        deserializers.put(Float.class, FloatCodec.instance);
        deserializers.put(double.class, NumberDeserializer.instance);
        deserializers.put(Double.class, NumberDeserializer.instance);
        deserializers.put(boolean.class, BooleanCodec.instance);
        deserializers.put(Boolean.class, BooleanCodec.instance);
        deserializers.put(Class.class, MiscCodec.instance);
        deserializers.put(char[].class, new CharArrayCodec());

        deserializers.put(AtomicBoolean.class, BooleanCodec.instance);
        deserializers.put(AtomicInteger.class, IntegerCodec.instance);
        deserializers.put(AtomicLong.class, LongCodec.instance);
        deserializers.put(AtomicReference.class, ReferenceCodec.instance);

        deserializers.put(WeakReference.class, ReferenceCodec.instance);
        deserializers.put(SoftReference.class, ReferenceCodec.instance);

        deserializers.put(UUID.class, MiscCodec.instance);
        deserializers.put(TimeZone.class, MiscCodec.instance);
        deserializers.put(Locale.class, MiscCodec.instance);
        deserializers.put(Currency.class, MiscCodec.instance);

        deserializers.put(Inet4Address.class, MiscCodec.instance);
        deserializers.put(Inet6Address.class, MiscCodec.instance);
        deserializers.put(InetSocketAddress.class, MiscCodec.instance);
        deserializers.put(File.class, MiscCodec.instance);
        deserializers.put(URI.class, MiscCodec.instance);
        deserializers.put(URL.class, MiscCodec.instance);
        deserializers.put(Pattern.class, MiscCodec.instance);
        deserializers.put(Charset.class, MiscCodec.instance);
        deserializers.put(JSONPath.class, MiscCodec.instance);
        deserializers.put(Number.class, NumberDeserializer.instance);
        deserializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        deserializers.put(AtomicLongArray.class, AtomicCodec.instance);
        deserializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);

        deserializers.put(Serializable.class, JavaObjectDeserializer.instance);
        deserializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        deserializers.put(Comparable.class, JavaObjectDeserializer.instance);
        deserializers.put(Closeable.class, JavaObjectDeserializer.instance);

        deserializers.put(JSONPObject.class, new JSONPDeserializer());
    }

    private static String[] splitItemsFormProperty(final String property ){
        if (property != null && property.length() > 0) {
            return property.split(",");
        }
        return null;
    }

    public void configFromPropety(Properties properties) {
        {
            String property = properties.getProperty(DENY_PROPERTY);
            String[] items = splitItemsFormProperty(property);
            addItemsToDeny(items);
        }
        {
            String property = properties.getProperty(AUTOTYPE_ACCEPT);
            String[] items = splitItemsFormProperty(property);
            addItemsToAccept(items);
        }
        {
            String property = properties.getProperty(AUTOTYPE_SUPPORT_PROPERTY);
            if ("true".equals(property)) {
                this.autoTypeSupport = true;
            } else if ("false".equals(property)) {
                this.autoTypeSupport = false;
            }
        }
    }

    private void addItemsToDeny(final String[] items){
        if (items == null){
            return;
        }

        for (int i = 0; i < items.length; ++i) {
            String item = items[i];
            this.addDeny(item);
        }
    }

    private void addItemsToAccept(final String[] items){
        if (items == null){
            return;
        }

        for (int i = 0; i < items.length; ++i) {
            String item = items[i];
            this.addAccept(item);
        }
    }

    public boolean isAutoTypeSupport() {
        return autoTypeSupport;
    }

    public void setAutoTypeSupport(boolean autoTypeSupport) {
        this.autoTypeSupport = autoTypeSupport;
    }

    public boolean isAsmEnable() {
        return asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    /**
     * @deprecated
     */
    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return deserializers;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDeserializers() {
        return deserializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer deserializer = get(type);
        if (deserializer != null) {
            return deserializer;
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
        ObjectDeserializer deserializer = get(type);
        if (deserializer != null) {
            return deserializer;
        }

        if (type == null) {
            type = clazz;
        }

        deserializer = get(type);
        if (deserializer != null) {
            return deserializer;
        }

        {
            JSONType annotation = TypeUtils.getAnnotation(clazz,JSONType.class);
            if (annotation != null) {
                Class<?> mappingTo = annotation.mappingTo();
                if (mappingTo != Void.class) {
                    return getDeserializer(mappingTo, mappingTo);
                }
            }
        }

        if (type instanceof WildcardType || type instanceof TypeVariable || type instanceof ParameterizedType) {
            deserializer = get(clazz);
        }

        if (deserializer != null) {
            return deserializer;
        }

        for (Module module : modules) {
            deserializer = module.createDeserializer(this, clazz);
            if (deserializer != null) {
                putDeserializer(type, deserializer);
                return deserializer;
            }
        }

        String className = clazz.getName();
        className = className.replace('$', '.');

        if (className.startsWith("java.awt.") //
            && AwtCodec.support(clazz)) {
            if (!awtError) {
                String[] names = new String[] {
                        "java.awt.Point",
                        "java.awt.Font",
                        "java.awt.Rectangle",
                        "java.awt.Color"
                };

                try {
                    for (String name : names) {
                        if (name.equals(className)) {
                            putDeserializer(Class.forName(name), deserializer = AwtCodec.instance);
                            return deserializer;
                        }
                    }
                } catch (Throwable e) {
                    // skip
                    awtError = true;
                }

                deserializer = AwtCodec.instance;
            }
        }

        if (!jdk8Error) {
            try {
                if (className.startsWith("java.time.")) {
                    String[] names = new String[] {
                            "java.time.LocalDateTime",
                            "java.time.LocalDate",
                            "java.time.LocalTime",
                            "java.time.ZonedDateTime",
                            "java.time.OffsetDateTime",
                            "java.time.OffsetTime",
                            "java.time.ZoneOffset",
                            "java.time.ZoneRegion",
                            "java.time.ZoneId",
                            "java.time.Period",
                            "java.time.Duration",
                            "java.time.Instant"
                    };

                    for (String name : names) {
                        if (name.equals(className)) {
                            putDeserializer(Class.forName(name), deserializer = Jdk8DateCodec.instance);
                            return deserializer;
                        }
                    }
                } else if (className.startsWith("java.util.Optional")) {
                    String[] names = new String[] {
                            "java.util.Optional",
                            "java.util.OptionalDouble",
                            "java.util.OptionalInt",
                            "java.util.OptionalLong"
                    };
                    for (String name : names) {
                        if (name.equals(className)) {
                            putDeserializer(Class.forName(name), deserializer = OptionalCodec.instance);
                            return deserializer;
                        }
                    }
                }
            } catch (Throwable e) {
                // skip
                jdk8Error = true;
            }
        }

        if (!jodaError) {
            try {
                if (className.startsWith("org.joda.time.")) {
                    String[] names = new String[] {
                            "org.joda.time.DateTime",
                            "org.joda.time.LocalDate",
                            "org.joda.time.LocalDateTime",
                            "org.joda.time.LocalTime",
                            "org.joda.time.Instant",
                            "org.joda.time.Period",
                            "org.joda.time.Duration",
                            "org.joda.time.DateTimeZone",
                            "org.joda.time.format.DateTimeFormatter"
                    };

                    for (String name : names) {
                        if (name.equals(className)) {
                            putDeserializer(Class.forName(name), deserializer = JodaCodec.instance);
                            return deserializer;
                        }
                    }
                }
            } catch (Throwable e) {
                // skip
                jodaError = true;
            }
        }

        if ((!guavaError) //
                && className.startsWith("com.google.common.collect.")) {
            try {
                String[] names = new String[] {
                        "com.google.common.collect.HashMultimap",
                        "com.google.common.collect.LinkedListMultimap",
                        "com.google.common.collect.LinkedHashMultimap",
                        "com.google.common.collect.ArrayListMultimap",
                        "com.google.common.collect.TreeMultimap"
                };

                for (String name : names) {
                    if (name.equals(className)) {
                        putDeserializer(Class.forName(name), deserializer = GuavaCodec.instance);
                        return deserializer;
                    }
                }
            } catch (ClassNotFoundException e) {
                // skip
                guavaError = true;
            }
        }

        if (className.equals("java.nio.ByteBuffer")) {
            putDeserializer(clazz, deserializer = ByteBufferCodec.instance);
        }

        if (className.equals("java.nio.file.Path")) {
            putDeserializer(clazz, deserializer = MiscCodec.instance);
        }

        if (clazz == Map.Entry.class) {
            putDeserializer(clazz, deserializer = MiscCodec.instance);
        }

        if (className.equals("org.javamoney.moneta.Money")) {
            putDeserializer(clazz, deserializer = MonetaCodec.instance);
        }

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            for (AutowiredObjectDeserializer autowired : ServiceLoader.load(AutowiredObjectDeserializer.class,
                                                                            classLoader)) {
                for (Type forType : autowired.getAutowiredFor()) {
                    putDeserializer(forType, autowired);
                }
            }
        } catch (Exception ex) {
            // skip
        }

        if (deserializer == null) {
            deserializer = get(type);
        }

        if (deserializer != null) {
            return deserializer;
        }

        if (clazz.isEnum()) {
            if (jacksonCompatible) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (TypeUtils.isJacksonCreator(method)) {
                        deserializer = createJavaBeanDeserializer(clazz, type);
                        putDeserializer(type, deserializer);
                        return deserializer;
                    }
                }
            }

            Class<?> deserClass = null;
            JSONType jsonType = TypeUtils.getAnnotation(clazz, JSONType.class);
            if (jsonType != null) {
                deserClass = jsonType.deserializer();
                try {
                    deserializer = (ObjectDeserializer) deserClass.newInstance();
                    putDeserializer(clazz, deserializer);
                    return deserializer;
                } catch (Throwable error) {
                    // skip
                }
            }

            deserializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            deserializer = ObjectArrayCodec.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class
                   || clazz == ArrayList.class) {
            deserializer = CollectionCodec.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            deserializer = CollectionCodec.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            deserializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            deserializer = new ThrowableDeserializer(this, clazz);
        } else if (PropertyProcessable.class.isAssignableFrom(clazz)) {
            deserializer = new PropertyProcessableDeserializer((Class<PropertyProcessable>) clazz);
        } else if (clazz == InetAddress.class) {
            deserializer = MiscCodec.instance;
        } else {
            deserializer = createJavaBeanDeserializer(clazz, type);
        }

        putDeserializer(type, deserializer);

        return deserializer;
    }

    /**
     *
     * @since 1.2.25
     */
    public void initJavaBeanDeserializers(Class<?>... classes) {
        if (classes == null) {
            return;
        }

        for (Class<?> type : classes) {
            if (type == null) {
                continue;
            }
            ObjectDeserializer deserializer = createJavaBeanDeserializer(type, type);
            putDeserializer(type, deserializer);
        }
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        boolean asmEnable = this.asmEnable & !this.fieldBased;
        if (asmEnable) {
            JSONType jsonType = TypeUtils.getAnnotation(clazz,JSONType.class);

            if (jsonType != null) {
                Class<?> deserializerClass = jsonType.deserializer();
                if (deserializerClass != Void.class) {
                    try {
                        Object deseralizer = deserializerClass.newInstance();
                        if (deseralizer instanceof ObjectDeserializer) {
                            return (ObjectDeserializer) deseralizer;
                        }
                    } catch (Throwable e) {
                        // skip
                    }
                }

                asmEnable = jsonType.asm();
            }

            if (asmEnable) {
                Class<?> superClass = JavaBeanInfo.getBuilderClass(clazz, jsonType);
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
        }

        if (clazz.getTypeParameters().length != 0) {
            asmEnable = false;
        }

        if (asmEnable && asmFactory != null && asmFactory.classLoader.isExternalClass(clazz)) {
            asmEnable = false;
        }

        if (asmEnable) {
            asmEnable = ASMUtils.checkName(clazz.getSimpleName());
        }

        if (asmEnable) {
            if (clazz.isInterface()) {
                asmEnable = false;
            }
            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz
                    , type
                    , propertyNamingStrategy
                    ,false
                    , TypeUtils.compatibleWithJavaBean
                    , jacksonCompatible
            );

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
                    break;
                }

                if (fieldInfo.getMember() != null //
                    && !ASMUtils.checkName(fieldInfo.getMember().getName())) {
                    asmEnable = false;
                    break;
                }

                JSONField annotation = fieldInfo.getAnnotation();
                if (annotation != null //
                    && ((!ASMUtils.checkName(annotation.name())) //
                        || annotation.format().length() != 0 //
                        || annotation.deserializeUsing() != Void.class //
                        || annotation.unwrapped())
                        || (fieldInfo.method != null && fieldInfo.method.getParameterTypes().length > 1)) {
                    asmEnable = false;
                    break;
                }

                if (fieldClass.isEnum()) { // EnumDeserializer
                    ObjectDeserializer fieldDeser = this.getDeserializer(fieldClass);
                    if (!(fieldDeser instanceof EnumDeserializer)) {
                        asmEnable = false;
                        break;
                    }
                }
            }
        }

        if (asmEnable) {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                asmEnable = false;
            }
        }

        if (asmEnable) {
            if (TypeUtils.isXmlField(clazz)) {
                asmEnable = false;
            }
        }

        if (!asmEnable) {
            return new JavaBeanDeserializer(this, clazz, type);
        }

        JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type, propertyNamingStrategy);
        try {
            return asmFactory.createJavaBeanDeserializer(this, beanInfo);
            // } catch (VerifyError e) {
            // e.printStackTrace();
            // return new JavaBeanDeserializer(this, clazz, type);
        } catch (NoSuchMethodException ex) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (JSONException asmError) {
            return new JavaBeanDeserializer(this, beanInfo);
        } catch (Exception e) {
            throw new JSONException("create asm deserializer error, " + clazz.getName(), e);
        }
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, //
                                                     JavaBeanInfo beanInfo, //
                                                     FieldInfo fieldInfo) {
        Class<?> clazz = beanInfo.clazz;
        Class<?> fieldClass = fieldInfo.fieldClass;

        Class<?> deserializeUsing = null;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            deserializeUsing = annotation.deserializeUsing();
            if (deserializeUsing == Void.class) {
                deserializeUsing = null;
            }
        }

        if (deserializeUsing == null && (fieldClass == List.class || fieldClass == ArrayList.class)) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }

        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        Type mixin = JSON.getMixInAnnotations(type);
        if (mixin != null) {
            IdentityHashMap<Type, ObjectDeserializer> mixInClasses = this.mixInDeserializers.get(type);
            if (mixInClasses == null) {
                //多线程下可能会重复创建，但不影响正确性
                mixInClasses = new IdentityHashMap<Type, ObjectDeserializer>(4);
                this.mixInDeserializers.put(type, mixInClasses);
            }
            mixInClasses.put(mixin, deserializer);
        } else {
            this.deserializers.put(type, deserializer);
        }
    }

    public ObjectDeserializer get(Type type) {
        Type mixin = JSON.getMixInAnnotations(type);
        if (null == mixin) {
            return this.deserializers.get(type);
        }
        IdentityHashMap<Type, ObjectDeserializer> mixInClasses = this.mixInDeserializers.get(type);
        if (mixInClasses == null) {
            return null;
        }
        return mixInClasses.get(mixin);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    /**
     * @deprecated  internal method, dont call
     */
    public boolean isPrimitive(Class<?> clazz) {
        return isPrimitive2(clazz);
    }

    /**
     * @deprecated  internal method, dont call
     */
    public static boolean isPrimitive2(Class<?> clazz) {
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
               || clazz.isEnum() //
        ;
    }

    /**
     * fieldName,field ，先生成fieldName的快照，减少之后的findField的轮询
     *
     * @param clazz
     * @param fieldCacheMap :map&lt;fieldName ,Field&gt;
     */
    public static void  parserAllFieldToCache(Class<?> clazz,Map</**fieldName*/String , Field> fieldCacheMap){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldCacheMap.containsKey(fieldName)) {
                fieldCacheMap.put(fieldName, field);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            parserAllFieldToCache(clazz.getSuperclass(), fieldCacheMap);
        }
    }

    public static Field getFieldFromCache(String fieldName, Map<String, Field> fieldCacheMap) {
        Field field = fieldCacheMap.get(fieldName);

        if (field == null) {
            field = fieldCacheMap.get("_" + fieldName);
        }

        if (field == null) {
            field = fieldCacheMap.get("m_" + fieldName);
        }

        if (field == null) {
            char c0 = fieldName.charAt(0);
            if (c0 >= 'a' && c0 <= 'z') {
                char[] chars = fieldName.toCharArray();
                chars[0] -= 32; // lower
                String fieldNameX = new String(chars);
                field = fieldCacheMap.get(fieldNameX);
            }

            if (fieldName.length() > 2) {
                char c1 = fieldName.charAt(1);
                if (fieldName.length() > 2
                        && c0 >= 'a' && c0 <= 'z'
                        && c1 >= 'A' && c1 <= 'Z') {
                    for (Map.Entry<String, Field> entry : fieldCacheMap.entrySet()) {
                        if (fieldName.equalsIgnoreCase(entry.getKey())) {
                            field = entry.getValue();
                            break;
                        }
                    }
                }
            }
        }

        return field;
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

        long hash = TypeUtils.fnv1a_64(name);
        if (Arrays.binarySearch(this.denyHashCodes, hash) >= 0) {
            return;
        }

        long[] hashCodes = new long[this.denyHashCodes.length + 1];
        hashCodes[hashCodes.length - 1] = hash;
        System.arraycopy(this.denyHashCodes, 0, hashCodes, 0, this.denyHashCodes.length);
        Arrays.sort(hashCodes);
        this.denyHashCodes = hashCodes;
    }

    public void addAccept(String name) {
        if (name == null || name.length() == 0) {
            return;
        }

        long hash = TypeUtils.fnv1a_64(name);
        if (Arrays.binarySearch(this.acceptHashCodes, hash) >= 0) {
            return;
        }

        long[] hashCodes = new long[this.acceptHashCodes.length + 1];
        hashCodes[hashCodes.length - 1] = hash;
        System.arraycopy(this.acceptHashCodes, 0, hashCodes, 0, this.acceptHashCodes.length);
        Arrays.sort(hashCodes);
        this.acceptHashCodes = hashCodes;
    }

    public Class<?> checkAutoType(Class type) {
        if (get(type) != null) {
            return type;
        }

        return checkAutoType(type.getName(), null, JSON.DEFAULT_PARSER_FEATURE);
    }

    public Class<?> checkAutoType(String typeName, Class<?> expectClass) {
        return checkAutoType(typeName, expectClass, JSON.DEFAULT_PARSER_FEATURE);
    }

    public Class<?> checkAutoType(String typeName, Class<?> expectClass, int features) {
        if (typeName == null) {
            return null;
        }

        if (typeName.length() >= 192 || typeName.length() < 3) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final boolean expectClassFlag;
        if (expectClass == null) {
            expectClassFlag = false;
        } else {
            if (expectClass == Object.class
                    || expectClass == Serializable.class
                    || expectClass == Cloneable.class
                    || expectClass == Closeable.class
                    || expectClass == EventListener.class
                    || expectClass == Iterable.class
                    || expectClass == Collection.class
                    ) {
                expectClassFlag = false;
            } else {
                expectClassFlag = true;
            }
        }

        String className = typeName.replace('$', '.');
        Class<?> clazz = null;

        final long BASIC = 0xcbf29ce484222325L;
        final long PRIME = 0x100000001b3L;

        final long h1 = (BASIC ^ className.charAt(0)) * PRIME;
        if (h1 == 0xaf64164c86024f1aL) { // [
            throw new JSONException("autoType is not support. " + typeName);
        }

        if ((h1 ^ className.charAt(className.length() - 1)) * PRIME == 0x9198507b5af98f0L) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long h3 = (((((BASIC ^ className.charAt(0))
                * PRIME)
                ^ className.charAt(1))
                * PRIME)
                ^ className.charAt(2))
                * PRIME;

        if (autoTypeSupport || expectClassFlag) {
            long hash = h3;
            for (int i = 3; i < className.length(); ++i) {
                hash ^= className.charAt(i);
                hash *= PRIME;
                if (Arrays.binarySearch(acceptHashCodes, hash) >= 0) {
                    clazz = TypeUtils.loadClass(typeName, defaultClassLoader, true);
                    if (clazz != null) {
                        return clazz;
                    }
                }
                if (Arrays.binarySearch(denyHashCodes, hash) >= 0 && TypeUtils.getClassFromMapping(typeName) == null) {
                    throw new JSONException("autoType is not support. " + typeName);
                }
            }
        }

        if (clazz == null) {
            clazz = TypeUtils.getClassFromMapping(typeName);
        }

        if (clazz == null) {
            clazz = deserializers.findClass(typeName);
        }

        if (clazz == null) {
            clazz = typeMapping.get(typeName);
        }

        if (clazz != null) {
            if (expectClass != null
                    && clazz != java.util.HashMap.class
                    && !expectClass.isAssignableFrom(clazz)) {
                throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
            }

            return clazz;
        }

        if (!autoTypeSupport) {
            long hash = h3;
            for (int i = 3; i < className.length(); ++i) {
                char c = className.charAt(i);
                hash ^= c;
                hash *= PRIME;

                if (Arrays.binarySearch(denyHashCodes, hash) >= 0) {
                    throw new JSONException("autoType is not support. " + typeName);
                }

                // white list
                if (Arrays.binarySearch(acceptHashCodes, hash) >= 0) {
                    if (clazz == null) {
                        clazz = TypeUtils.loadClass(typeName, defaultClassLoader, true);
                    }

                    if (expectClass != null && expectClass.isAssignableFrom(clazz)) {
                        throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                    }

                    return clazz;
                }
            }
        }

        boolean jsonType = false;
        InputStream is = null;
        try {
            String resource = typeName.replace('.', '/') + ".class";
            if (defaultClassLoader != null) {
                is = defaultClassLoader.getResourceAsStream(resource);
            } else {
                is = ParserConfig.class.getClassLoader().getResourceAsStream(resource);
            }
            if (is != null) {
                ClassReader classReader = new ClassReader(is, true);
                TypeCollector visitor = new TypeCollector("<clinit>", new Class[0]);
                classReader.accept(visitor);
                jsonType = visitor.hasJsonType();
            }
        } catch (Exception e) {
            // skip
        } finally {
            IOUtils.close(is);
        }

        final int mask = Feature.SupportAutoType.mask;
        boolean autoTypeSupport = this.autoTypeSupport
                || (features & mask) != 0
                || (JSON.DEFAULT_PARSER_FEATURE & mask) != 0;

        if (clazz == null && (autoTypeSupport || jsonType || expectClassFlag)) {
            boolean cacheClass = autoTypeSupport || jsonType;
            clazz = TypeUtils.loadClass(typeName, defaultClassLoader, cacheClass);
        }

        if (clazz != null) {
            if (jsonType) {
                TypeUtils.addMapping(typeName, clazz);
                return clazz;
            }

            if (ClassLoader.class.isAssignableFrom(clazz) // classloader is danger
                    || javax.sql.DataSource.class.isAssignableFrom(clazz) // dataSource can load jdbc driver
                    || javax.sql.RowSet.class.isAssignableFrom(clazz) //
                    ) {
                throw new JSONException("autoType is not support. " + typeName);
            }

            if (expectClass != null) {
                if (expectClass.isAssignableFrom(clazz)) {
                    TypeUtils.addMapping(typeName, clazz);
                    return clazz;
                } else {
                    throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                }
            }

            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, clazz, propertyNamingStrategy);
            if (beanInfo.creatorConstructor != null && autoTypeSupport) {
                throw new JSONException("autoType is not support. " + typeName);
            }
        }

        if (!autoTypeSupport) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        if (clazz != null) {
            TypeUtils.addMapping(typeName, clazz);
        }

        return clazz;
    }

    public void clearDeserializers() {
        this.deserializers.clear();
        this.initDeserializers();
    }

    public boolean isJacksonCompatible() {
        return jacksonCompatible;
    }

    public void setJacksonCompatible(boolean jacksonCompatible) {
        this.jacksonCompatible = jacksonCompatible;
    }

    public void register(String typeName, Class type) {
        typeMapping.putIfAbsent(typeName, type);
    }

    public void register(Module module) {
        this.modules.add(module);
    }
}
