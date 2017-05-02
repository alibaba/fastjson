package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

public class GiftGood implements Serializable {
    private static final long serialVersionUID = -5565479370390105553L;

    private String giftSkuId;
    private long giftGoodsId;
    private int actualStore;
    private long freeNumber;
    private long realBuyCount;
    private String title;
    private int giftType;

    public long getGiftGoodsId() {
        return giftGoodsId;
    }

    public void setGiftGoodsId(long giftGoodsId) {
        this.giftGoodsId = giftGoodsId;
    }

    public String getGiftSkuId() {
        return giftSkuId;
    }

    public void setGiftSkuId(String giftSkuId) {
        this.giftSkuId = giftSkuId;
    }

    public long getFreeNumber() {
        return freeNumber;
    }

    public void setFreeNumber(long freeNumber) {
        this.freeNumber = freeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getActualStore() {
        return actualStore;
    }

    public void setActualStore(int actualStore) {
        this.actualStore = actualStore;
    }

    public long getRealBuyCount() {
        return realBuyCount;
    }

    public void setRealBuyCount(long realBuyCount) {
        this.realBuyCount = realBuyCount;
    }
}