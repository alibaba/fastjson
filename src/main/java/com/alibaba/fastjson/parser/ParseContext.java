package com.alibaba.fastjson.parser;

public class ParseContext {

    private final Object       object;
    private final ParseContext parent;

    public ParseContext(ParseContext parent, Object object){
        super();
        this.parent = parent;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public ParseContext getParentContext() {
        return parent;
    }

}
