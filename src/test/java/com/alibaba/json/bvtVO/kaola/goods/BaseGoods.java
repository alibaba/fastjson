package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * 包含主要数据的商品基类
 *
 * Created by xinyang on 2016/7/5.
 */
public class BaseGoods extends SpringSubModule implements Serializable {

    private static final long serialVersionUID = 7816363397552520228L;
    private long goodsId;
    private String goodsNumLabel;
    private String title;
    private String averagePriceLable;
    private String priceLabel;
    private float originalPrice;
    private float currentPrice;
    private int onlineStatus;
    private int actualStorageStatus;
    private String smallSingleActivityLabelUrl;
    private String smallActivityLabel;
    private String customLabel;
    private int isAppPriceOnlyLabel;
    private int showColorCard;
    private String recReason;
    private String imgUrl;
    private List<String> imgUrlList;

    public BaseGoods() {
        springType = TYPE_GOODS;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNumLabel() {
        return goodsNumLabel;
    }

    public void setGoodsNumLabel(String goodsNumLabel) {
        this.goodsNumLabel = goodsNumLabel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAveragePriceLable() {
        return averagePriceLable;
    }

    public void setAveragePriceLable(String averagePriceLable) {
        this.averagePriceLable = averagePriceLable;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getActualStorageStatus() {
        return actualStorageStatus;
    }

    public void setActualStorageStatus(int actualStorageStatus) {
        this.actualStorageStatus = actualStorageStatus;
    }

    public String getSmallSingleActivityLabelUrl() {
        return smallSingleActivityLabelUrl;
    }

    public void setSmallSingleActivityLabelUrl(String smallSingleActivityLabelUrl) {
        this.smallSingleActivityLabelUrl = smallSingleActivityLabelUrl;
    }

    public String getSmallActivityLabel() {
        return smallActivityLabel;
    }

    public void setSmallActivityLabel(String smallActivityLabel) {
        this.smallActivityLabel = smallActivityLabel;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }

    public int getIsAppPriceOnlyLabel() {
        return isAppPriceOnlyLabel;
    }

    public void setIsAppPriceOnlyLabel(int isAppPriceOnlyLabel) {
        this.isAppPriceOnlyLabel = isAppPriceOnlyLabel;
    }

    public int getShowColorCard() {
        return showColorCard;
    }

    public void setShowColorCard(int showColorCard) {
        this.showColorCard = showColorCard;
    }

    public String getRecReason() {
        return recReason;
    }

    public void setRecReason(String recReason) {
        this.recReason = recReason;
    }

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }
}
