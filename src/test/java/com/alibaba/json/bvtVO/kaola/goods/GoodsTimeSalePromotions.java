package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

public class GoodsTimeSalePromotions implements Serializable {

    private static final long serialVersionUID = -4013236527262092068L;
    private int type;
    private String title;
    private float price;
    private long startTime;
    private long endTime;
    private String averagePriceLabel;
    private int canBuy;
    private String link;
    private String currentTitle;
    private String currentLabel;

    public String getCurrentLabel() {
        return currentLabel;
    }

    public void setCurrentLabel(String currentLabel) {
        this.currentLabel = currentLabel;
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getAveragePriceLabel() {
        return averagePriceLabel;
    }

    public void setAveragePriceLabel(String averagePriceLabel) {
        this.averagePriceLabel = averagePriceLabel;
    }

    public int getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(int canBuy) {
        this.canBuy = canBuy;
    }
}
