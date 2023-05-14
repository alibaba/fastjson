package com.alibaba.fastjson.deserializer.issue2711;

import com.alibaba.fastjson.annotation.JSONField;

public class PageRequest<T> {
    @JSONField(unwrapped = true)
    T data;
    int from = 0;
    int size = 10;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}