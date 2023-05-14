package com.alibaba.json.bvtVO.bbc;


import java.io.Serializable;

public class BaseResult<T> implements Serializable {
    private T data;
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    
    
    public BaseResult() {
        
    }
}