package com.alibaba.fastjson.serializer;

/**
 * @since 1.1.35
 */
public abstract class AfterFilter implements SerializeFilter {

    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<JSONSerializer>();
    private static final ThreadLocal<Character> separatorLocal = new ThreadLocal<Character>();

    private final static Character                   COMMA           = ',';

    final char writeAfter(JSONSerializer serializer, Object object, char separator) {
        JSONSerializer last = serializerLocal.get();
        serializerLocal.set(serializer);
        separatorLocal.set(separator);
        writeAfter(object);
        serializerLocal.set(last);
        return separatorLocal.get();
    }

    protected final void writeKeyValue(String key, Object value) {
        JSONSerializer serializer = serializerLocal.get();
        char separator = separatorLocal.get();

        boolean ref = serializer.containsReference(value);
        serializer.writeKeyValue(separator, key, value);
        if (!ref && serializer.references != null) {
            serializer.references.remove(value);
        }
        if (separator != ',') {
            separatorLocal.set(COMMA);
        }
    }

    public abstract void writeAfter(Object object);
}
