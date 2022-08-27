package com.alibaba.fastjson;

class JSONStreamContext {

    final static int                  StartObject   = 1001;
    final static int                  PropertyKey   = 1002;
    final static int                  PropertyValue = 1003;
    final static int                  StartArray    = 1004;
    final static int                  ArrayValue    = 1005;

    protected final JSONStreamContext parent;

    protected int                     state;

    public JSONStreamContext(JSONStreamContext parent, int state){
        this.parent = parent;
        this.state = state;
    }
}
