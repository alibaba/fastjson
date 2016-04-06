package com.alibaba.fastjson.serializer;

public class SerialContext {

    public final SerialContext parent;

    public final Object        object;

    public final Object        fieldName;

    public final int                 features;

    private int                 fieldFeatures;

    public SerialContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
        this.features = features;
        this.fieldFeatures = fieldFeatures;
    }

    public String toString() {
        if (parent == null) {
            return "$";
        } else {
            if (fieldName instanceof Integer) {
                return parent.toString() + "[" + fieldName + "]";
            } else {
                return parent.toString() + "." + fieldName;
            }

        }
    }

    public int getFeatures() {
        return features;
    }

    public boolean isEnabled(SerializerFeature feature) {
        int mask = feature.mask;
        return (features & mask) != 0 || (fieldFeatures & mask) != 0;
    }
}
