package com.alibaba.fastjson.serializer;

public class SerialContext {

    private final SerialContext parent;

    private final Object        object;

    private final Object        fieldName;
    
    private       String        path;

    public SerialContext(SerialContext parent, Object object, Object fieldName){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
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
        if (path != null) {
            return path;
        }
        if (parent == null) {
            path = "$";
        } else {
            if (fieldName instanceof Integer) {
                path = parent.getPath() + "[" + fieldName + "]";
            } else {
                path = parent.getPath() + "." + fieldName;
            }

        }
        return path;
    }

    public String toString() {
        return getPath();
    }
}
