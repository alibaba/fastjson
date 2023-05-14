package com.derbysoft.spitfire.fastjson;

import java.io.Serializable;
import java.util.List;

public class Generic<T> implements Serializable{
        String header;
        T payload;

//        List<T>

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}