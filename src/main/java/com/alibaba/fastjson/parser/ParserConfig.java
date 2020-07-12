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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
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
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ParserConfig {
    private static long[] denyList = new long[] {
            0x80D0C70BCC2FEA02L,
            0x86FC2BF9BEAF7AEFL,
            0x87F52A1B07EA33A6L,
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
            0xAAAA0826487A3737L,
            0xAC6262F52C98AA39L,
            0xAD937A449831E8A0L,
            0xAE50DA1FAD60A096L,
            0xAFFF4C95B99A334DL,
            0xB40F341C746EC94FL,
            0xB7E8ED757F5D13A2L,
            0xB98B6B5396932FE9L,
            0xBCDD9DC12766F0CEL,
            0xBEBA72FB1CCBA426L,
            0xC00BE1DEBAF2808BL,
            0xC1086AFAE32E6258L,
            0xC2664D0958ECFE4CL,
            0xC41FF7C9C87C7C05L,
            0xC664B363BACA050AL,
            0xC7599EBFE3E72406L,
            0xC8D49E5601E661A9L,
            0xC963695082FD728EL,
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
            0xE9184BE55B1D962AL,
            0xE9F20BAD25F60807L,
            0xF2983D099D29B477L,
            0xF3702A4A5490B8E8L,
            0xF474E44518F26736L,
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
            0xB6E292FA5955ADEL,
            0xEE6511B66FD5EF0L,
            0x100150A253996624L,
            0x10B2BDCA849D9B3EL,
            0x10DBC48446E0DAE5L,
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
            0x34A81EE78429FDF1L,
            0x378307CB0111E878L,
            0x3826F4B2380C8B9BL,
            0x398F942E01920CF0L,
            0x3A31412DBB05C7FFL,
            0x3ADBA40367F73264L,
            0x3B0B51ECBF6DB221L,
            0x42D11A560FC9FBA9L,
            0x43320DC9D2AE0892L,
            0x440E89208F445FB9L,
            0x46C808A4B5841F57L,
            0x49312BDAFB0077D9L,
            0x4A3797B30328202CL,
            0x4BA3E254E758D70DL,
            0x4BF881E49D37F530L,
            0x4CF54EEC05E3E818L,
            0x4DA972745FEB30C1L,
            0x4EF08C90FF16C675L,
            0x4FD10DDC6D13821FL,
            0x527DB6B46CE3BCBCL,
            0x535E552D6F9700C1L,
            0x5728504A6D454FFCL,
            0x599B5C1213A099ACL,
            0x5A5BD85C072E5EFEL,
            0x5AB0CB3071AB40D1L,
            0x5B6149820275EA42L,
            0x5D74D3E5B9370476L,
            0x5D92E6DDDE40ED84L,
            0x5F215622FB630753L,
            0x61C5BDD721385107L,
            0x62DB241274397C34L,
            0x63A220E60A17C7B9L,
            0x647AB0224E149EBEL,
            0x65F81B84C1D920CDL,
            0x665C53C311193973L,
            0x6749835432E0F0D2L,
            0x69B6E0175084B377L,
            0x6A47501EBB2AFDB2L,
            0x6FCABF6FA54CAFFFL,
            0x746BD4A53EC195FBL,
            0x74B50BB9260E31FFL,
            0x75CC60F5871D0FD3L,
            0x767A586A5107FEEFL,
            0x7AA7EE3627A19CF3L,
            0x7ED9311D28BF1A65L,
            0x7ED9481D28BF417AL
    };

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public static ParserConfig                        global      = new ParserConfig();
    private final IdentityHashMap<ObjectDeserializer> deserializers = new IdentityHashMap<ObjectDeserializer>(1024);
    public final  SymbolTable                         symbolTable = new SymbolTable(16384);
    public        ClassLoader                         defaultClassLoader;
    public        PropertyNamingStrategy              propertyNamingStrategy;
    public        boolean                             autoTypeSupport;

    public ParserConfig(){
        deserializers.put(SimpleDateFormat.class, MiscCodec.instance);
        deserializers.put(java.util.Date.class, DateCodec.instance);
        deserializers.put(Calendar.class, DateCodec.instance);

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
        deserializers.put(char.class, MiscCodec.instance);
        deserializers.put(Character.class, MiscCodec.instance);
        deserializers.put(byte.class, NumberCodec.instance);
        deserializers.put(Byte.class, NumberCodec.instance);
        deserializers.put(short.class, NumberCodec.instance);
        deserializers.put(Short.class, NumberCodec.instance);
        deserializers.put(int.class, IntegerCodec.instance);
        deserializers.put(Integer.class, IntegerCodec.instance);
        deserializers.put(long.class, IntegerCodec.instance);
        deserializers.put(Long.class, IntegerCodec.instance);
        deserializers.put(BigInteger.class, BigDecimalCodec.instance);
        deserializers.put(BigDecimal.class, BigDecimalCodec.instance);
        deserializers.put(float.class, NumberCodec.instance);
        deserializers.put(Float.class, NumberCodec.instance);
        deserializers.put(double.class, NumberCodec.instance);
        deserializers.put(Double.class, NumberCodec.instance);
        deserializers.put(boolean.class, BooleanCodec.instance);
        deserializers.put(Boolean.class, BooleanCodec.instance);
        deserializers.put(Class.class, MiscCodec.instance);
        deserializers.put(char[].class, ArrayCodec.instance);
        deserializers.put(Object[].class, ArrayCodec.instance);

        deserializers.put(UUID.class, MiscCodec.instance);
        deserializers.put(TimeZone.class, MiscCodec.instance);
        deserializers.put(Locale.class, MiscCodec.instance);
        deserializers.put(Currency.class, MiscCodec.instance);
        deserializers.put(URI.class, MiscCodec.instance);
        deserializers.put(URL.class, MiscCodec.instance);
        deserializers.put(Pattern.class, MiscCodec.instance);
        deserializers.put(Charset.class, MiscCodec.instance);
        deserializers.put(Number.class, NumberCodec.instance);
        deserializers.put(StackTraceElement.class, MiscCodec.instance);

        deserializers.put(Serializable.class, JavaObjectDeserializer.instance);
        deserializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        deserializers.put(Comparable.class, JavaObjectDeserializer.instance);
        deserializers.put(Closeable.class, JavaObjectDeserializer.instance);

    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = this.deserializers.get(type);
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
        ObjectDeserializer deserializer = deserializers.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        if (type == null) {
            type = clazz;
        }

        deserializer = deserializers.get(type);
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
            deserializer = deserializers.get(clazz);
        }

        if (deserializer != null) {
            return deserializer;
        }

        deserializer = deserializers.get(type);
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
        ObjectDeserializer deserializer = deserializers.get(clazz);
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
        return deserializers.get(clazz) != null;
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
        deserializers.put(type, deserializer);
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

    public Class<?> checkAutoType(String typeName, Class<?> expectClass, int features) {
        if (typeName == null) {
            return null;
        }

        if (typeName.length() >= 128 || typeName.length() < 3) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long BASIC = 0xcbf29ce484222325L;
        final long PRIME = 0x100000001b3L;

        final long h1 = (BASIC ^ typeName.charAt(0)) * PRIME;
        if (h1 == 0xaf64164c86024f1aL) { // [
            throw new JSONException("autoType is not support. " + typeName);
        }

        if ((h1 ^ typeName.charAt(typeName.length() - 1)) * PRIME == 0x9198507b5af98f0L) {
            throw new JSONException("autoType is not support. " + typeName);
        }

        final long h3 = (((((BASIC ^ typeName.charAt(0))
                * PRIME)
                ^ typeName.charAt(1))
                * PRIME)
                ^ typeName.charAt(2))
                * PRIME;

        long hash = h3;
        for (int i = 3; i < typeName.length(); ++i) {
            hash ^= typeName.charAt(i);
            hash *= PRIME;
            if (Arrays.binarySearch(denyList, hash) >= 0 && TypeUtils.getClassFromMapping(typeName) == null) {
                throw new JSONException("autoType is not support. " + typeName);
            }
        }

        Class<?> clazz = TypeUtils.getClassFromMapping(typeName);
        if (clazz != null) {
            return clazz;
        }

        clazz = deserializers.findClass(typeName);
        if (clazz != null) {
            return clazz;
        }

        clazz = TypeUtils.loadClass(typeName, defaultClassLoader, false);

        if (clazz != null
            && expectClass != null
            && clazz != java.util.HashMap.class)
        {
            if (expectClass.isAssignableFrom(clazz)) {
                TypeUtils.addMapping(typeName, clazz);
                return clazz;
            } else {
                throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
            }
        }

        if (clazz.isAnnotationPresent(JSONType.class)) {
            TypeUtils.addMapping(typeName, clazz);
            return clazz;
        }

        final int mask = Feature.SupportAutoType.mask;
        if ((features & mask) == 0
                && (JSON.DEFAULT_PARSER_FEATURE & mask) == 0
                && !autoTypeSupport) {
            throw new JSONException("autoType is not support : " + typeName);
        }

        TypeUtils.addMapping(typeName, clazz);

        return clazz;
    }
}
