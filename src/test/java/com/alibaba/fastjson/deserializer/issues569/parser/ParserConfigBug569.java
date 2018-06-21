package com.alibaba.fastjson.deserializer.issues569.parser;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.*;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.*;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-10-10
 */
public class ParserConfigBug569 extends ParserConfig {

    private static boolean                                  awtError    = false;
    private static boolean                                  jdk8Error   = false;
    private String[]                                        denyList    = new String[] { "java.lang.Thread" };

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

        return new DefaultFieldDeserializerBug569(mapping, clazz, fieldInfo);
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        com.alibaba.fastjson.util.IdentityHashMap<Type, ObjectDeserializer> derializers = super.getDeserializers();
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
            JSONType annotation = TypeUtils.getAnnotation(clazz,JSONType.class);
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

        String className = clazz.getName();
        className = className.replace('$', '.');
        for (int i = 0; i < denyList.length; ++i) {
            String deny = denyList[i];
            if (className.startsWith(deny)) {
                throw new JSONException("parser deny : " + className);
            }
        }

        if (className.startsWith("java.awt.") //
                && AwtCodec.support(clazz)) {
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

                derializer = AwtCodec.instance;
            }
        }

        if (!jdk8Error) {
            try {
                if (className.startsWith("java.time.")) {

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

                    derializer = derializers.get(clazz);
                } else if (className.startsWith("java.util.Optional")) {

                    derializers.put(Class.forName("java.util.Optional"), OptionalCodec.instance);
                    derializers.put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
                    derializers.put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
                    derializers.put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);

                    derializer = derializers.get(clazz);
                }
            } catch (Throwable e) {
                // skip
                jdk8Error = true;
            }
        }

        if (className.equals("java.nio.file.Path")) {
            derializers.put(clazz, MiscCodec.instance);
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

        if (derializer == null) {
            derializer = derializers.get(type);
        }

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
            derializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }

        putDeserializer(type, derializer);

        return derializer;
    }

}
