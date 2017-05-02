package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhoulei
 * @time 2015-06-16
 */
public class GoodsPropertyList implements Serializable {

    private static final long serialVersionUID = 9126459934788577903L;
    /**
     * isFilter : 1
     * propertyValues : []
     * isDisplay : 0
     * searchBoost : 100
     * propertyNameId : 348
     * isLogistics : 0
     * showArea : 1
     * showOrder : 25
     * isColor : 1
     * propertyNameCn : 网络制式
     * isSku : 1
     */
    private List<PropertyValues> propertyValues;
    private int isDisplay;
    private String propertyNameId;
    private int isColor;
    private String propertyNameCn;


    public void setPropertyValues(List<PropertyValues> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    public void setPropertyNameId(String propertyNameId) {
        this.propertyNameId = propertyNameId;
    }

    public void setIsColor(int isColor) {
        this.isColor = isColor;
    }

    public void setPropertyNameCn(String propertyNameCn) {
        this.propertyNameCn = propertyNameCn;
    }

    public List<PropertyValues> getPropertyValues() {
        return propertyValues;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public String getPropertyNameId() {
        return propertyNameId;
    }

    public int getIsColor() {
        return isColor;
    }

    public String getPropertyNameCn() {
        return propertyNameCn;
    }
}
