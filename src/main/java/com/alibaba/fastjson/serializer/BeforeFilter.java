package com.alibaba.fastjson.serializer;

public abstract class BeforeFilter implements SerializeFilter {

    private final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<JSONSerializer>();
    private final ThreadLocal<Character>        commaLocal      = new ThreadLocal<Character>();

    private final static Character            COMMA           = Character.valueOf(',');

    final char writeBefore(JSONSerializer serializer, Object object, char commaFlag) {
        serializerLocal.set(serializer);
        commaLocal.set(commaFlag);
        writeBefore(object);
        serializerLocal.set(null);
        return commaLocal.get();
    }

    public abstract void writeBefore(Object object);

    public final void writeKeyValue(String key, Object value) {
        JSONSerializer serializer = serializerLocal.get();
        char comma = commaLocal.get();
        serializer.writeKeyValue(comma, key, value);
        if (comma != ',') {
            commaLocal.set(COMMA);
        }
    }

}
