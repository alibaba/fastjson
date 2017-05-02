package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

public class SkuProperty implements Serializable{

    private static final long serialVersionUID = -4742207894762278260L;
    private String propertyName;
    private String propertyValue;

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}
