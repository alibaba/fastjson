package com.alibaba.fastjson.deserializer.issues569.beans;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-09-06
 */
public class MyResponse<T> {

    Boolean success;
    Integer errCode;
    String errDes;
    T       result;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrDes() {
        return errDes;
    }

    public void setErrDes(String errDes) {
        this.errDes = errDes;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
