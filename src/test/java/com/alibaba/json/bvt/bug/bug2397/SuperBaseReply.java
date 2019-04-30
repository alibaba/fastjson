package com.alibaba.json.bvt.bug.bug2397;

import java.util.List;

public class SuperBaseReply<T> {
    private List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
