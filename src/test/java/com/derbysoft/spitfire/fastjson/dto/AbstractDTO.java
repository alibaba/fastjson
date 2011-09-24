package com.derbysoft.spitfire.fastjson.dto;

import java.io.Serializable;

public abstract class AbstractDTO implements Serializable {
    private boolean keepgoingValidate = false;

    private boolean checkCircularReference = true;

    public boolean isKeepgoingValidate() {
        return keepgoingValidate;
    }

    public void setKeepgoingValidate(boolean keepgoingValidate) {
        this.keepgoingValidate = keepgoingValidate;
    }

    public boolean isCheckCircularReference() {
        return checkCircularReference;
    }

    public void setCheckCircularReference(boolean checkCircularReference) {
        this.checkCircularReference = checkCircularReference;
    }
}
