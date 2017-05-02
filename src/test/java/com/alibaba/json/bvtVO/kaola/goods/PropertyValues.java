package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * 商品propertyValues信息
 *
 * @author zhoulei
 * @time 2015-06-16
 */
public class PropertyValues implements Serializable{

    private static final long serialVersionUID = -786630039637464856L;

    private String imageUrl;
    private String propertyValue;
    private String propertyValueId;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setPropertyValueId(String propertyValueId) {
        this.propertyValueId = propertyValueId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public String getPropertyValueId() {
        return propertyValueId;
    }
}
