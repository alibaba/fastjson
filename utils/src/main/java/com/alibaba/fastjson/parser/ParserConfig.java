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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.annotation.JSONCreator;
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

import static com.alibaba.fastjson.util.TypeUtils.fnv1a_64_magic_hashcode;
import static com.alibaba.fastjson.util.TypeUtils.fnv1a_64_magic_prime;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {

    public static final String    DENY_PROPERTY_INTERNAL    = "fastjson.parser.deny.internal";
    public static final String    DENY_PROPERTY             = "fastjson.parser.deny";
    public static final String    AUTOTYPE_ACCEPT           = "fastjson.parser.autoTypeAccept";
    public static final String    AUTOTYPE_SUPPORT_PROPERTY = "fastjson.parser.autoTypeSupport";
    public static final String    SAFE_MODE_PROPERTY        = "fastjson.parser.safeMode";

    public static  final String[] DENYS_INTERNAL;
    public static  final String[] DENYS;
    private static final String[] AUTO_TYPE_ACCEPT_LIST;
    public static  final boolean  AUTO_SUPPORT;
    public static  final boolean  SAFE_MODE;
    private static final long[]   INTERNAL_WHITELIST_HASHCODES;

    static  {
        {
            String property = IOUtils.getStringProperty(DENY_PROPERTY_INTERNAL);
            DENYS_INTERNAL = splitItemsFormProperty(property);
        }
        {
            String property = IOUtils.getStringProperty(DENY_PROPERTY);
            DENYS = splitItemsFormProperty(property);
        }
        {
            String property = IOUtils.getStringProperty(AUTOTYPE_SUPPORT_PROPERTY);
            AUTO_SUPPORT = "true".equals(property);
        }
        {
            String property = IOUtils.getStringProperty(SAFE_MODE_PROPERTY);
            SAFE_MODE = "true".equals(property);
        }
        {
            String property = IOUtils.getStringProperty(AUTOTYPE_ACCEPT);
            String[] items = splitItemsFormProperty(property);
            if (items == null) {
                items = new String[0];
            }
            AUTO_TYPE_ACCEPT_LIST = items;
        }

        INTERNAL_WHITELIST_HASHCODES = new long[] {
                0x9F2E20FB6049A371L,
                0xA8AAA929446FFCE4L,
                0xD45D6F8C9017FAL,
                0x64DC636F343516DCL
        };
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
    private long[]                                          internalDenyHashCodes;
    private long[]                                          denyHashCodes;
    private long[]                                          acceptHashCodes;


    public final boolean                                    fieldBased;
    private boolean                                         jacksonCompatible     = false;

    public boolean                                          compatibleWithJavaBean = TypeUtils.compatibleWithJavaBean;
    private List<Module>                                    modules                = new ArrayList<Module>();
    private volatile List<AutoTypeCheckHandler>             autoTypeCheckHandlers;
    private boolean                                         safeMode               = SAFE_MODE;

    {
        denyHashCodes = new long[]{
                0x80D0C70BCC2FEA02L,
                0x868385095A22725FL,
                0x86FC2BF9BEAF7AEFL,
                0x87F52A1B07EA33A6L,
                0x8872F29FD0B0B7A7L,
                0x8BAAEE8F9BF77FA7L,
                0x8EADD40CB2A94443L,
                0x8F75F9FA0DF03F80L,
                0x9172A53F157930AFL,
                0x92122D710E364FB8L,
                0x941866E73BEFF4C9L,
                0x94305C26580F73C5L,
                0x9437792831DF7D3FL,
                0xA123A62F93178B20L,
                0xA85882CE1044C450L,
                0xAA3DAFFDB10C4937L,
                0xAAA9E6B7C1E1C6A7L,
                0xAAAA0826487A3737L,
                0xAB82562F53E6E48FL,
                0xAC6262F52C98AA39L,
                0xAD937A449831E8A0L,
                0xAE50DA1FAD60A096L,
                0xAFF6FF23388E225AL,
                0xAFFF4C95B99A334DL,
                0xB40F341C746EC94FL,
                0xB7E8ED757F5D13A2L,
                0xB98B6B5396932FE9L,
                0xBCDD9DC12766F0CEL,
                0xBCE0DEE34E726499L,
                0xBE4F13E96A6796D0L,
                0xBEBA72FB1CCBA426L,
                0xC00BE1DEBAF2808BL,
                0xC1086AFAE32E6258L,
                0xC2664D0958ECFE4CL,
                0xC41FF7C9C87C7C05L,
                0xC664B363BACA050AL,
                0xC7599EBFE3E72406L,
                0xC8D49E5601E661A9L,
                0xC8F04B3A28909935L,
                0xC963695082FD728EL,
                0xCBF29CE484222325L,
                0xD1EFCDF4B3316D34L,
                0xD54B91CC77B239EDL,
                0xD59EE91F0B09EA01L,
                0xD66F68AB92E7FEF5L,
                0xD8CA3D595E982BACL,
                0xDCD8D615A6449E3EL,
                0xDE23A0809A8B9BD6L,
                0xDEFC208F237D4104L,
                0xDF2DDFF310CDB375L,
                0xE09AE4604842582FL,
                0xE1919804D5BF468FL,
                0xE2EB3AC7E56C467EL,
                0xE603D6A51FAD692BL,
                0xE704FD19052B2A34L,
                0xE9184BE55B1D962AL,
                0xE9F20BAD25F60807L,
                0xED13653CB45C4BEDL,
                0xF2983D099D29B477L,
                0xF3702A4A5490B8E8L,
                0xF474E44518F26736L,
                0xF4D93F4FB3E3D991L,
                0xF5D77DCF8E4D71E6L,
                0xF6C0340E73A36A69L,
                0xF7E96E74DFA58DBCL,
                0xFC773AE20C827691L,
                0xFCF3E78644B98BD8L,
                0xFD5BFC610056D720L,
                0xFFA15BF021F1E37CL,
                0xFFDD1A80F1ED3405L,
                0x10E067CD55C5E5L,
                0x761619136CC13EL,
                0x22BAA234C5BFB8AL,
                0x3085068CB7201B8L,
                0x45B11BC78A3ABA3L,
                0x55CFCA0F2281C07L,
                0xA555C74FE3A5155L,
                0xB6E292FA5955ADEL,
                0xBEF8514D0B79293L,
                0xEE6511B66FD5EF0L,
                0x100150A253996624L,
                0x10B2BDCA849D9B3EL,
                0x10DBC48446E0DAE5L,
                0x119B5B1F10210AFCL,
                0x144277B467723158L,
                0x14DB2E6FEAD04AF0L,
                0x154B6CB22D294CFAL,
                0x17924CCA5227622AL,
                0x193B2697EAAED41AL,
                0x1CD6F11C6A358BB7L,
                0x1E0A8C3358FF3DAEL,
                0x24652CE717E713BBL,
                0x24D2F6048FEF4E49L,
                0x24EC99D5E7DC5571L,
                0x25E962F1C28F71A2L,
                0x275D0732B877AF29L,
                0x28AC82E44E933606L,
                0x2A71CE2CC40A710CL,
                0x2AD1CE3A112F015DL,
                0x2ADFEFBBFE29D931L,
                0x2B3A37467A344CDFL,
                0x2B6DD8B3229D6837L,
                0x2D308DBBC851B0D8L,
                0x2FE950D3EA52AE0DL,
                0x313BB4ABD8D4554CL,
                0x327C8ED7C8706905L,
                0x332F0B5369A18310L,
                0x339A3E0B6BEEBEE9L,
                0x33C64B921F523F2FL,
                0x33E7F3E02571B153L,
                0x34A81EE78429FDF1L,
                0x37317698DCFCE894L,
                0x378307CB0111E878L,
                0x3826F4B2380C8B9BL,
                0x398F942E01920CF0L,
                0x3A31412DBB05C7FFL,
                0x3A7EE0635EB2BC33L,
                0x3ADBA40367F73264L,
                0x3B0B51ECBF6DB221L,
                0x3BF14094A524F0E2L,
                0x42D11A560FC9FBA9L,
                0x43320DC9D2AE0892L,
                0x440E89208F445FB9L,
                0x46C808A4B5841F57L,
                0x470FD3A18BB39414L,
                0x49312BDAFB0077D9L,
                0x4A3797B30328202CL,
                0x4BA3E254E758D70DL,
                0x4BF881E49D37F530L,
                0x4CF54EEC05E3E818L,
                0x4DA972745FEB30C1L,
                0x4EF08C90FF16C675L,
                0x4FD10DDC6D13821FL,
                0x521B4F573376DF4AL,
                0x527DB6B46CE3BCBCL,
                0x535E552D6F9700C1L,
                0x54855E265FE1DAD5L,
                0x5728504A6D454FFCL,
                0x599B5C1213A099ACL,
                0x5A5BD85C072E5EFEL,
                0x5AB0CB3071AB40D1L,
                0x5B6149820275EA42L,
                0x5D74D3E5B9370476L,
                0x5D92E6DDDE40ED84L,
                0x5E61093EF8CDDDBBL,
                0x5F215622FB630753L,
                0x61C5BDD721385107L,
                0x62DB241274397C34L,
                0x636ECCA2A131B235L,
                0x63A220E60A17C7B9L,
                0x647AB0224E149EBEL,
                0x65F81B84C1D920CDL,
                0x665C53C311193973L,
                0x6749835432E0F0D2L,
                0x69B6E0175084B377L,
                0x6A47501EBB2AFDB2L,
                0x6FCABF6FA54CAFFFL,
                0x6FE92D83FC0A4628L,
                0x746BD4A53EC195FBL,
                0x74B50BB9260E31FFL,
                0x75CC60F5871D0FD3L,
                0x767A586A5107FEEFL,
                0x78E5935826671397L,
                0x793ADDDED7A967F5L,
                0x7AA7EE3627A19CF3L,
                0x7AFA070241B8CC4BL,
                0x7ED9311D28BF1A65L,
                0x7ED9481D28BF417AL,
                0x7EE6C477DA20BBE3L
        };

        long[] hashCodes = new long[AUTO_TYPE_ACCEPT_LIST.length];
        for (int i = 0; i < AUTO_TYPE_ACCEPT_LIST.length; i++) {
            hashCodes[i] = TypeUtils.fnv1a_64(AUTO_TYPE_ACCEPT_LIST[i]);
        }

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
        addItemsToDeny0(DENYS_INTERNAL);
        addItemsToAccept(AUTO_TYPE_ACCEPT_LIST);

    }

    private final Callable<Void> initDeserializersWithJavaSql = new Callable<Void>() {
        public Void call() {
            deserializers.put(java.sql.Timestamp.class, SqlDateDeserializer.instance_timestamp);
            deserializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
            deserializers.put(java.sql.Time.class, TimeDeserializer.instance);
            deserializers.put(java.util.Date.class, DateCodec.instance);
            return null;
        }
    };

    private void initDeserializers() {
        deserializers.put(SimpleDateFormat.class, MiscCodec.instance);
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
        ModuleUtil.callWhenHasJavaSql(initDeserializersWithJavaSql);
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

    private void addItemsToDeny0(final String[] items){
        if (items == null){
            return;
        }

        for (int i = 0; i < items.length; ++i) {
            String item = items[i];
            this.addDenyInternal(item);
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

    /**
     * @since 1.2.68
     */
    public boolean isSafeMode() {
        return safeMode;
    }

    /**
     * @since 1.2.68
     */
    public void setSafeMode(boolean safeMode) {
        this.safeMode = safeMode;
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
        if (deserializer == null && type instanceof ParameterizedTypeImpl) {
            Type innerType = TypeReference.intern((ParameterizedTypeImpl) type);
            deserializer = get(innerType);
        }

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

            Class mixInType = (Class) JSON.getMixInAnnotations(clazz);

            Class<?> deserClass = null;
            JSONType jsonType = TypeUtils.getAnnotation(mixInType != null ? mixInType : clazz, JSONType.class);

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

            Method jsonCreatorMethod = null;
            if (mixInType != null) {
                Method mixedCreator = getEnumCreator(mixInType, clazz);
                if (mixedCreator != null) {
                    try {
                        jsonCreatorMethod = clazz.getMethod(mixedCreator.getName(), mixedCreator.getParameterTypes());
                    } catch (Exception e) {
                        // skip
                    }
                }
            } else {
                jsonCreatorMethod = getEnumCreator(clazz, clazz);
            }

            if (jsonCreatorMethod != null) {
                deserializer = new EnumCreatorDeserializer(jsonCreatorMethod);
                putDeserializer(clazz, deserializer);
                return deserializer;
            }

            deserializer = getEnumDeserializer(clazz);
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

    private static Method getEnumCreator(Class clazz, Class enumClass) {
        Method[] methods = clazz.getMethods();
        Method jsonCreatorMethod = null;
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())
                    && method.getReturnType() == enumClass
                    && method.getParameterTypes().length == 1
            ) {
                JSONCreator jsonCreator = method.getAnnotation(JSONCreator.class);
                if (jsonCreator != null) {
                    jsonCreatorMethod = method;
                    break;
                }
            }
        }

        return jsonCreatorMethod;
    }

    /**
     * 可以通过重写这个方法，定义自己的枚举反序列化实现
     * @param clazz 转换的类型
     * @return 返回一个枚举的反序列化实现
     * @author zhu.xiaojie
     * @time 2020-4-5
     */
    protected ObjectDeserializer getEnumDeserializer(Class<?> clazz){
        return new EnumDeserializer(clazz);
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

                asmEnable = jsonType.asm()
                        && jsonType.parseFeatures().length == 0;
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
                        || annotation.parseFeatures().length != 0 //
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

    private static Function<Class<?>, Boolean> isPrimitiveFuncation = new Function<Class<?>, Boolean>() {
        public Boolean apply(Class<?> clazz) {
            return clazz == java.sql.Date.class //
                    || clazz == java.sql.Time.class //
                    || clazz == java.sql.Timestamp.class;
        }
    };

    /**
     * @deprecated  internal method, dont call
     */
    public static boolean isPrimitive2(final Class<?> clazz) {
        Boolean primitive = clazz.isPrimitive() //
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
                || clazz.isEnum() //
                ;
        if (!primitive) {
            primitive = ModuleUtil.callWhenHasJavaSql(isPrimitiveFuncation, clazz);
        }
        return primitive != null ? primitive : false;
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
                if (c0 >= 'a' && c0 <= 'z'
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

    public void addDenyInternal(String name) {
        if (name == null || name.length() == 0) {
            return;
        }

        long hash = TypeUtils.fnv1a_64(name);
        if (internalDenyHashCodes == null) {
            this.internalDenyHashCodes = new long[] {hash};
            return;
        }

        if (Arrays.binarySearch(this.internalDenyHashCodes, hash) >= 0) {
            return;
        }

        long[] hashCodes = new long[this.internalDenyHashCodes.length + 1];
        hashCodes[hashCodes.length - 1] = hash;
        System.arraycopy(this.internalDenyHashCodes, 0, hashCodes, 0, this.internalDenyHashCodes.length);
        Arrays.sort(hashCodes);
        this.internalDenyHashCodes = hashCodes;
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

        if (autoTypeCheckHandlers != null) {
            for (AutoTypeCheckHandler h : autoTypeCheckHandlers) {
                Class<?> type = h.handler(typeName, expectClass, features);
                if (type != null) {
                    return type;
                }
            }
        }

        final int safeModeMask = Feature.SafeMode.mask;
        boolean safeMode = this.safeMode
                || (features & safeModeMask) != 0
                || (JSON.DEFAULT_PARSER_FEATURE & safeModeMask) != 0;
        if (safeMode) {
            throw new JSONException("safeMode not support autoType : " + typeName);
        }

        final int mask = Feature.SupportAutoType.mask;
        boolean autoTypeSupport = this.autoTypeSupport
                || (features & mask) != 0
                || (JSON.DEFAULT_PARSER_FEATURE & mask) != 0;

        if (typeName.length() >= 192 || typeName.length() < 3) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final boolean expectClassFlag;
        if (expectClass == null) {
            expectClassFlag = false;
        } else {
            long expectHash = TypeUtils.fnv1a_64(expectClass.getName());
            if (expectHash == 0x90a25f5baa21529eL
                    || expectHash == 0x2d10a5801b9d6136L
                    || expectHash == 0xaf586a571e302c6bL
                    || expectHash == 0xed007300a7b227c6L
                    || expectHash == 0x295c4605fd1eaa95L
                    || expectHash == 0x47ef269aadc650b4L
                    || expectHash == 0x6439c4dff712ae8bL
                    || expectHash == 0xe3dd9875a2dc5283L
                    || expectHash == 0xe2a8ddba03e69e0dL
                    || expectHash == 0xd734ceb4c3e9d1daL
            ) {
                expectClassFlag = false;
            } else {
                expectClassFlag = true;
            }
        }

        String className = typeName.replace('$', '.');
        Class<?> clazz;

        final long h1 = (fnv1a_64_magic_hashcode ^ className.charAt(0)) * fnv1a_64_magic_prime;
        if (h1 == 0xaf64164c86024f1aL) { // [
            throw new JSONException("autoType is not support. " + typeName);
        }

        if ((h1 ^ className.charAt(className.length() - 1)) * fnv1a_64_magic_prime == 0x9198507b5af98f0L) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long h3 = (((((fnv1a_64_magic_hashcode ^ className.charAt(0))
                * fnv1a_64_magic_prime)
                ^ className.charAt(1))
                * fnv1a_64_magic_prime)
                ^ className.charAt(2))
                * fnv1a_64_magic_prime;

        long fullHash = TypeUtils.fnv1a_64(className);
        boolean internalWhite = Arrays.binarySearch(INTERNAL_WHITELIST_HASHCODES,  fullHash) >= 0;

        if (internalDenyHashCodes != null) {
            long hash = h3;
            for (int i = 3; i < className.length(); ++i) {
                hash ^= className.charAt(i);
                hash *= fnv1a_64_magic_prime;
                if (Arrays.binarySearch(internalDenyHashCodes, hash) >= 0) {
                    throw new JSONException("autoType is not support. " + typeName);
                }
            }
        }

        if ((!internalWhite) && (autoTypeSupport || expectClassFlag)) {
            long hash = h3;
            for (int i = 3; i < className.length(); ++i) {
                hash ^= className.charAt(i);
                hash *= fnv1a_64_magic_prime;
                if (Arrays.binarySearch(acceptHashCodes, hash) >= 0) {
                    clazz = TypeUtils.loadClass(typeName, defaultClassLoader, true);
                    if (clazz != null) {
                        return clazz;
                    }
                }
                if (Arrays.binarySearch(denyHashCodes, hash) >= 0 && TypeUtils.getClassFromMapping(typeName) == null) {
                    if (Arrays.binarySearch(acceptHashCodes, fullHash) >= 0) {
                        continue;
                    }

                    throw new JSONException("autoType is not support. " + typeName);
                }
            }
        }

        clazz = TypeUtils.getClassFromMapping(typeName);

        if (clazz == null) {
            clazz = deserializers.findClass(typeName);
        }

        if (expectClass == null && clazz != null && Throwable.class.isAssignableFrom(clazz) && !autoTypeSupport) {
            clazz = null;
        }

        if (clazz == null) {
            clazz = typeMapping.get(typeName);
        }

        if (internalWhite) {
            clazz = TypeUtils.loadClass(typeName, defaultClassLoader, true);
        }

        if (clazz != null) {
            if (expectClass != null
                    && clazz != java.util.HashMap.class
                    && clazz != java.util.LinkedHashMap.class
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
                hash *= fnv1a_64_magic_prime;

                if (Arrays.binarySearch(denyHashCodes, hash) >= 0) {
                    if (typeName.endsWith("Exception") || typeName.endsWith("Error")) {
                        return null;
                    }

                    throw new JSONException("autoType is not support. " + typeName);
                }

                // white list
                if (Arrays.binarySearch(acceptHashCodes, hash) >= 0) {
                    clazz = TypeUtils.loadClass(typeName, defaultClassLoader, true);

                    if (clazz == null) {
                        return expectClass;
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

        if (autoTypeSupport || jsonType || expectClassFlag) {
            boolean cacheClass = autoTypeSupport || jsonType;
            clazz = TypeUtils.loadClass(typeName, defaultClassLoader, cacheClass);
        }

        if (clazz != null) {
            if (jsonType) {
                if (autoTypeSupport) {
                    TypeUtils.addMapping(typeName, clazz);
                }
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
                    if (autoTypeSupport) {
                        TypeUtils.addMapping(typeName, clazz);
                    }
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
            if (typeName.endsWith("Exception") || typeName.endsWith("Error")) {
                return null;
            }

            throw new JSONException("autoType is not support. " + typeName);
        }

        if (clazz != null) {
            if (autoTypeSupport) {
                TypeUtils.addMapping(typeName, clazz);
            }
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

    public void addAutoTypeCheckHandler(AutoTypeCheckHandler h) {
        List<AutoTypeCheckHandler> autoTypeCheckHandlers = this.autoTypeCheckHandlers;
        if (autoTypeCheckHandlers == null) {
            this.autoTypeCheckHandlers
                    = autoTypeCheckHandlers
                    = new CopyOnWriteArrayList();
        }

        autoTypeCheckHandlers.add(h);
    }

    /**
     * @since 1.2.68
     */
    public interface AutoTypeCheckHandler {
        Class<?> handler(String typeName, Class<?> expectClass, int features);
    }
}
