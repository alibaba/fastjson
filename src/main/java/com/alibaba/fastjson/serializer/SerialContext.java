package com.alibaba.fastjson.serializer;

public class SerialContext {

    private final SerialContext parent;

    private final Object        object;

    private final Object        fieldName;

    private int                 features = 0;
	private transient String path;

    public SerialContext(SerialContext parent, Object object, Object fieldName){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
    }

    public int getFeatures() {
        return features;
    }

    public void setFeatures(int features) {
        this.features = features;
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
		if(path == null) {
			path = calculatePath();
		}
		return path;
    }

	private String calculatePath() {
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
}
