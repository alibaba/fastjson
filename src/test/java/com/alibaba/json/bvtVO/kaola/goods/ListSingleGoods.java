package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

public class ListSingleGoods extends BaseGoods implements Serializable {

    private static final long serialVersionUID = -3438780275458197898L;

    private String upleftImgUrl;
    private int self;
    private String benefitPoint;
    private String averagePriceColor;
    private int commentCount;
    private String recReason;
    private String introduce;
    private boolean hasLiveTag;
    private int islike;
    private float productgrade;
    private List<String> attributeList;
    private int propertyShowType;

    @Override
    public String getRecReason() {
        return recReason;
    }

    @Override
    public void setRecReason(String recReason) {
        this.recReason = recReason;
    }

    public String getUpleftImgUrl() {
        return upleftImgUrl;
    }

    public void setUpleftImgUrl(String upleftImgUrl) {
        this.upleftImgUrl = upleftImgUrl;
    }

    public int getSelf() {
        return self;
    }

    public void setSelf(int self) {
        this.self = self;
    }

    public String getBenefitPoint() {
        return benefitPoint;
    }

    public void setBenefitPoint(String benefitPoint) {
        this.benefitPoint = benefitPoint;
    }

    public String getAveragePriceColor() {
        return averagePriceColor;
    }

    public void setAveragePriceColor(String averagePriceColor) {
        this.averagePriceColor = averagePriceColor;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public boolean getHasLiveTag() {
        return hasLiveTag;
    }

    public void setHasLiveTag(boolean hasLiveTag) {
        this.hasLiveTag = hasLiveTag;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public float getProductgrade() {
        return productgrade;
    }

    public void setProductgrade(float productgrade) {
        this.productgrade = productgrade;
    }

    public List<String> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<String> attributeList) {
        this.attributeList = attributeList;
    }

    public int getPropertyShowType() {
        return propertyShowType;
    }

    public void setPropertyShowType(int propertyShowType) {
        this.propertyShowType = propertyShowType;
    }
}
