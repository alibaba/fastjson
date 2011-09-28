package com.derbysoft.spitfire.fastjson.dto;


public class  GenericRS<T> extends AbstractRS{
    private ResponseHeader header;
    private T payload;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }
}
