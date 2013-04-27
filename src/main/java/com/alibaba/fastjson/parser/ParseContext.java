package com.alibaba.fastjson.parser;

public class ParseContext {

    private Object             object;
    private final ParseContext parent;
    private final Object       fieldName;

    public ParseContext(ParseContext parent, Object object, Object fieldName){
        super();
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ParseContext getParentContext() {
        return parent;
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
        return this.getPath();
    }
}
