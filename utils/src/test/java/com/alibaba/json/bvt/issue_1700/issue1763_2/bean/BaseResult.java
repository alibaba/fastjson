package com.alibaba.json.bvt.issue_1700.issue1763_2.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * BaseResult
 *
 * @author cnlyml
 */
public class BaseResult<T> {
    private static final Integer SUCCESS_CODE = 0;

    private Integer code;
    private String message;
    @JSONField(name = "content")
    private T content;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return code.equals(SUCCESS_CODE);
    }
}
