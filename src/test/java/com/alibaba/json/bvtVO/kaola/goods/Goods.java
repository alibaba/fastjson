package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * 商品信息
 *
 * @author wang.lucai
 * @time 2014-12-06
 */
public class Goods implements Serializable {
    private static final long serialVersionUID = -2381977322076365239L;

    /** 商品有库存 */
    public static final int GOODS_IN_STOCK = 1;

    /** 商品无库存 */
    public static final int GOODS_OUT_STOCK = 0;

    /** 换购商品 */
    public static final int GOODS_TYPE_EXCHANGE = 1;

    /** 商品ID */
    private String goodsId;

    /** 商品标题 */
    private String title;

    private String shortTitle;

    /** 商品描述 */
    private String introduce;

    /** 商品图片URL */
    private String goodsPictureUrl;

    /** 商品图片170URL */
    private String imageUrl;

    /** 商品来源 */
    private String goodsSource;

    /** 商品指导价格 */
    private float goodsGuidePrice;

    /** 商品市场价格 带有删除线的价格 */
    private float goodsMarketPrice;

    /** 商品折扣信息 */
    private float tuanDiscount;

    /** 商品库存状态 */
    private int actualStorageStatus;

    /** 是否为换购商品 */
    /*private int mIsChangeBuy;*/

    /** 商品类型 */
    private int goodsType;

    /** 商品类型描述 */
    private String goodsTypeDescription;

    /** 商品重量 */
    private float weight;

    /** 规格id */
    private String skuId;

    /**
     * 商品信息无参构造函数
     */
    public Goods() {

    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getTitle() {
        return title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public String getGoodsPictureUrl() {
        return goodsPictureUrl;
    }

    public String getGoodsSource() {
        return goodsSource;
    }

    public float getGoodsGuidePrice() {
        return goodsGuidePrice;
    }

    public float getGoodsMarketPrice() {
        return goodsMarketPrice;
    }

    public float getTuanDiscount() {
        return tuanDiscount;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public void setTitle(String mGoodsTitle) {
        this.title = mGoodsTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public void setGoodsPictureUrl(String mGoodsPictureUrl) {
        this.goodsPictureUrl = mGoodsPictureUrl;
    }

    public void setGoodsSource(String mGoodsOriginLabel) {
        this.goodsSource = mGoodsOriginLabel;
    }

    public void setGoodsGuidePrice(float mGoodsGuidePrice) {
        this.goodsGuidePrice = mGoodsGuidePrice;
    }

    public void setGoodsMarketPrice(float mGoodsMarketPrice) {
        this.goodsMarketPrice = mGoodsMarketPrice;
    }

    public void setTuanDiscount(float mDiscount) {
        this.tuanDiscount = mDiscount;
    }

    public void setIntroduce(String description) {
        introduce = description;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setActualStorageStatus(int storageStatus) {
        actualStorageStatus = storageStatus;
    }

    public int getActualStorageStatus() {
        return actualStorageStatus;
    }

    /*public int getIsChangeBuy() {
        return mIsChangeBuy;
    }

    public void setIsChangeBuy(int isChangeBuy) {
        this.mIsChangeBuy = isChangeBuy;
    }*/

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeDescription() {
        return goodsTypeDescription;
    }

    public void setGoodsTypeDescription(String goodsTypeDescription) {
        this.goodsTypeDescription = goodsTypeDescription;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float mWeight) {
        this.weight = mWeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}