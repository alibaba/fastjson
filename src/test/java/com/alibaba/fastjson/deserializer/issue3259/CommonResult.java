package com.alibaba.fastjson.deserializer.issue3259;

public class CommonResult<T> {

    private boolean success;

    private T resultData;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }
}