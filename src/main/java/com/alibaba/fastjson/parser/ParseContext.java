package com.alibaba.fastjson.parser;

import java.lang.reflect.Type;

public class ParseContext {

    public Object             object;
    public final ParseContext parent;
    public final Object       fieldName;
    public Type               type;

    public ParseContext(ParseContext parent, Object object, Object fieldName){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
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
