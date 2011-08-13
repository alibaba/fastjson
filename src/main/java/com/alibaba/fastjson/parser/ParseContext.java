package com.alibaba.fastjson.parser;

public class ParseContext {

    private Object             object;
    private final ParseContext parent;

    public ParseContext(ParseContext parent, Object object){
        super();
        this.parent = parent;
        this.object = object;
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

}
