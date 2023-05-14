package com.alibaba.json.bvtVO.wuqi;

/**
 * Created by wuqi on 17/3/30.
 */
public class Result<T> {
    private T data;


    public Result(){}

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                '}';
    }
}
