package com.alibaba.fastjson.serializer;

public class SerialContext {
    private final SerialContextHierarchy hierarchy;
    private final SerialContextToStringGenerator stringGenerator;

    public SerialContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures){
        this.hierarchy = new SerialContextHierarchy(parent, object, fieldName, features);
        this.stringGenerator = new SerialContextToStringGenerator(parent, object, fieldName);
    }

    public String toString() {
        return stringGenerator.toString();
    }

    /**
     * @deprecated
     */
    public SerialContext getParent() {
        return hierarchy.getParent();
    }

    /**
     * @deprecated
     */
    public Object getObject() {
        return hierarchy.getObject();
    }

    /**
     * @deprecated
     */
    public Object getFieldName() {
        return hierarchy.getFieldName();
    }

    /**
     * @deprecated
     */
    public String getPath() {
        return toString();
    }
}
