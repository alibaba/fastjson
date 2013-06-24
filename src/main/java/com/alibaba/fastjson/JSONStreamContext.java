package com.alibaba.fastjson;

class JSONStreamContext {

    final static int               StartObject   = 1001;
    final static int               PropertyKey   = 1002;
    final static int               PropertyValue = 1003;
    final static int               StartArray    = 1004;
    final static int               ArrayValue    = 1005;

    private final JSONStreamContext parent;

    private int                     state;

    public JSONStreamContext(JSONStreamContext parent, int state){
        this.parent = parent;
        this.state = state;
    }

    public JSONStreamContext getParent() {
        return parent;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
