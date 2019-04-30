package com.alibaba.json.bvt.bug.bug2397;


import java.util.List;

public class BaseReply<T, R> extends SuperBaseReply<Msg, T> {
    private List<R> items2;

    public List<R> getItems2() {
        return items2;
    }

    public void setItems2(List<R> items2) {
        this.items2 = items2;
    }
}
