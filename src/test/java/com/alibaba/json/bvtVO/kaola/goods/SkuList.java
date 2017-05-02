package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhoulei
 * @time 2015-06-16
 */
public class SkuList implements Serializable {

    private static final long serialVersionUID = 6064325201240868922L;
    private int goodsId;
    private double taxRate;
    private List<String> skuPropertyValueIdList;
    private int currentPrice;
    private float actualCurrentPrice;
    private String skuId;
    private int actualStorageStatus;    //活动之后的实际库存状态，1库存有货；0库存无货；
    private int actualStore;            //活动之后的实际库存数量
    private int preSale;                //该SKU是否预售 0：否， 1：是
    private String preSaleDesc;         //预售商品描述
    private float maturityPrice;        //限时促销的到期的时候的价格
    private int maturityStore;          //APP3.4版本 限时促销开始时的活动库存
    private String floatContent;        //浮层秒杀库存文案

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public void setSkuPropertyValueIdList(List<String> skuPropertyValueIdList) {
        this.skuPropertyValueIdList = skuPropertyValueIdList;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setActualCurrentPrice(float actualCurrentPrice) {
        this.actualCurrentPrice = actualCurrentPrice;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public void setActualStorageStatus(int actualStorageStatus) {
        this.actualStorageStatus = actualStorageStatus;
    }

    public void setActualStore(int actualStore) {
        this.actualStore = actualStore;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public List<String> getSkuPropertyValueIdList() {
        return skuPropertyValueIdList;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public float getActualCurrentPrice() {
        return actualCurrentPrice;
    }

    public String getSkuId() {
        return skuId;
    }

    public int getActualStorageStatus() {
        return actualStorageStatus;
    }

    public int getActualStore() {
        return actualStore;
    }

    public int getPreSale() {
        return preSale;
    }

    public void setPreSale(int preSale) {
        this.preSale = preSale;
    }

    public String getPreSaleDesc() {
        return preSaleDesc;
    }

    public void setPreSaleDesc(String preSaleDesc) {
        this.preSaleDesc = preSaleDesc;
    }

    public float getMaturityPrice() {
        return maturityPrice;
    }

    public void setMaturityPrice(float maturityPrice) {
        this.maturityPrice = maturityPrice;
    }

    public int getMaturityStore() {
        return maturityStore;
    }

    public void setMaturityStore(int maturityStore) {
        this.maturityStore = maturityStore;
    }

    public String getFloatContent() {
        return floatContent;
    }

    public void setFloatContent(String floatContent) {
        this.floatContent = floatContent;
    }
}
