package com.alibaba.fastjson.serializer;

public abstract class BeforeFilter implements SerializeFilter {

    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<JSONSerializer>();
    // private 修饰的变量，可以重命名
    private static final ThreadLocal<Character> separatorLocal = new ThreadLocal<Character>();

    private final static Character                   COMMA           = ',';

    final char writeBefore(JSONSerializer serializer, Object object, char separator) {
        JSONSerializer last = serializerLocal.get();
        serializerLocal.set(serializer);
        separatorLocal.set(separator);
        writeBefore(object);
        serializerLocal.set(last);
        return separatorLocal.get();
    }

    protected final void writeKeyValue(String key, Object value) {
        JSONSerializer serializer = serializerLocal.get();
        char separator = separatorLocal.get();

        boolean ref = serializer.references.containsKey(value);
        serializer.writeKeyValue(separator, key, value);
        if (!ref) {
            serializer.references.remove(value);
        }

        if (separator != ',') {
            separatorLocal.set(COMMA);
        }
    }

    public abstract void writeBefore(Object object);
}
