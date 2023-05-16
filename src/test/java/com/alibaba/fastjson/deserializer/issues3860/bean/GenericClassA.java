package com.alibaba.fastjson.deserializer.issues3860.bean;

/**
 * @author Archer
 * @date 2022/05/06
 */

public class GenericClassA<T,R>{
    private T data;
    private R result;

    public GenericClassA() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }
}
