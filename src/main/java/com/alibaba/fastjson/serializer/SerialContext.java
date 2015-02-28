package com.alibaba.fastjson.serializer;

public class SerialContext {

    private final SerialContext parent;

    private final Object        object;

    private final Object        fieldName;

    private int                 features;

    private int                 fieldFeatures;

    public SerialContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
        this.features = features;
        this.fieldFeatures = fieldFeatures;
    }

    public SerialContext getParent() {
        return parent;
    }

    public Object getObject() {
        return object;
    }

    public Object getFieldName() {
        return fieldName;
    }

    public String getPath() {
        if (parent == null) {
            return "$";
        } else {
            if (fieldName instanceof Integer) {
                return parent.getPath() + "[" + fieldName + "]";
            } else {
                return parent.getPath() + "." + fieldName;
            }

        }
    }

    public String toString() {
        return getPath();
    }

    public int getFeatures() {
        return features;
    }

    public boolean isEnabled(SerializerFeature feature) {
        return SerializerFeature.isEnabled(features, fieldFeatures, feature);
    }
}
