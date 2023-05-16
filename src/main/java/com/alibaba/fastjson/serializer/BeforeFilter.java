package com.alibaba.fastjson.serializer;

public abstract class BeforeFilter implements SerializeFilter {

    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<JSONSerializer>();
    private static final ThreadLocal<Character>      seperatorLocal  = new ThreadLocal<Character>();

    private final static Character                   COMMA           = Character.valueOf(',');

    final char writeBefore(JSONSerializer serializer, Object object, char seperator) {
        JSONSerializer last = serializerLocal.get();
        serializerLocal.set(serializer);
        seperatorLocal.set(seperator);
        writeBefore(object);
        serializerLocal.set(last);
        return seperatorLocal.get();
    }

    protected final void writeKeyValue(String key, Object value) {
        JSONSerializer serializer = serializerLocal.get();
        char seperator = seperatorLocal.get();

        //CS304 Issue link: https://github.com/alibaba/fastjson/issues/4062
        //CS304 Issue link: https://github.com/alibaba/fastjson/issues/4152
        //ref can be null if set the SerializerFeature.DisableCircularReferenceDetect,
        //so use method 'containsReference' to judge if the serializer.reference is null before use method 'containsKey'
        //Otherwise, it will raise NullPointerException
        boolean ref = serializer.containsReference(value);
        serializer.writeKeyValue(seperator, key, value);
        //CS304 Issue link: https://github.com/alibaba/fastjson/issues/4062
        //CS304 Issue link: https://github.com/alibaba/fastjson/issues/4152
        //ref can be null if set the SerializerFeature.DisableCircularReferenceDetect,
        //so first judge if the serializer.reference is null before use method 'remove'
        //Otherwise, it will raise NullPointerException
        if (!ref&& serializer.references != null) {
            serializer.references.remove(value);
        }

        if (seperator != ',') {
            seperatorLocal.set(COMMA);
        }
    }

    public abstract void writeBefore(Object object);
}
