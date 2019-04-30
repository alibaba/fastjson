package com.alibaba.json.bvt.bug.bug2397;

import java.util.List;

public class SuperBaseReply<R, T> {
    private List<R> items;

    private T[] itemArray;

    public T[] getItemArray() {
        return itemArray;
    }

    public void setItemArray(T[] itemArray) {
        this.itemArray = itemArray;
    }

    public List<R> getItems() {
        return items;
    }

    public void setItems(List<R> items) {
        this.items = items;
    }
}
