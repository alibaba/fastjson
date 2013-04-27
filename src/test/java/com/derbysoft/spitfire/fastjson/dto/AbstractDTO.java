package com.derbysoft.spitfire.fastjson.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class AbstractDTO implements Serializable {
    private boolean keepgoingValidate = false;

    private boolean checkCircularReference = true;

    @JSONField(name="KV")
    public boolean isKeepgoingValidate() {
        return keepgoingValidate;
    }

    @JSONField(name="KV")
    public void setKeepgoingValidate(boolean keepgoingValidate) {
        this.keepgoingValidate = keepgoingValidate;
    }

    @JSONField(name="CR")
    public boolean isCheckCircularReference() {
        return checkCircularReference;
    }

    @JSONField(name="CR")
    public void setCheckCircularReference(boolean checkCircularReference) {
        this.checkCircularReference = checkCircularReference;
    }
}
