package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.util.IdentityHashMap;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author TimAndy
 */
public class IterableCodec implements ObjectSerializer {
    public final static IterableCodec instance = new IterableCodec();
    private final IdentityHashMap<Type, ObjectSerializer> serializers = new IdentityHashMap<Type, ObjectSerializer>(1024);//Iterable as bean use this field only

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        features |= out.features;

        //Iterable as array
        if (SerializerFeature.isEnabled(features, SerializerFeature.WriteIterableAsArray)) {
            if (object == null) {
                out.writeNull(SerializerFeature.WriteNullListAsEmpty);
                return;
            }
            out.write('[');
            Iterator<?> it = ((Iterable<?>) object).iterator();
            if (it.hasNext()) {
                serializer.write(it.next());
                while (it.hasNext()) {
                    out.write(',');
                    serializer.write(it.next());
                }
            }
            out.write(']');
            return;
        }

        //Iterable as bean
        if (object == null) {
            out.writeNull();
            return;
        }
        ObjectSerializer javaBeanSerializer = getJavaBeanSerializer(object.getClass(), serializer.getMapping());
        javaBeanSerializer.write(serializer, object, fieldName, fieldType, features);
    }

    private ObjectSerializer getJavaBeanSerializer(Class<?> clazz, SerializeConfig config) {
        ObjectSerializer serializer = serializers.get(clazz);
        if (serializer == null) {
            serializer = config.createJavaBeanSerializer(clazz);
            serializers.put(clazz, serializer);
        }
        return serializer;
    }
}
