package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by bojan on 2016/10/9.
 */

public class MemberGood implements Serializable {
    private static final long serialVersionUID = -5257639812981035566L;
    private float currentPrice;                 //会员价
    private float originPrice;                  //考拉价
    private String originGoodsUrl;              //考拉价商品（原商品）url
    private String priceTag;                    // 价格标签
    private String memberOnlyUrl;               //会员专享页URL

    public String getMemberOnlyUrl() {
        return memberOnlyUrl;
    }

    public void setMemberOnlyUrl(String memberOnlyUrl) {
        this.memberOnlyUrl = memberOnlyUrl;
    }

    public float getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(float originPrice) {
        this.originPrice = originPrice;
    }

    public String getOriginGoodsUrl() {
        return originGoodsUrl;
    }

    public void setOriginGoodsUrl(String originGoodsUrl) {
        this.originGoodsUrl = originGoodsUrl;
    }

    public String getPriceTag() {
        return priceTag;
    }

    public void setPriceTag(String priceTag) {
        this.priceTag = priceTag;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }
}
