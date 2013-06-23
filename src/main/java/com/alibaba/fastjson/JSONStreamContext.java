package com.alibaba.fastjson;

class JSONStreamContext {

    private final JSONStreamContext parent;

    private JSONStreamState         state;

    public JSONStreamContext(JSONStreamContext parent, JSONStreamState state){
        this.parent = parent;
        this.state = state;
    }

    public JSONStreamContext getParent() {
        return parent;
    }

    public JSONStreamState getState() {
        return state;
    }

    public void setState(JSONStreamState state) {
        this.state = state;
    }
}
