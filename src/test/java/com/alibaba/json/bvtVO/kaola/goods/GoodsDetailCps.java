package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * cps分享数据
 * Created by xinyang on 2015/11/23.
 */
public class GoodsDetailCps implements Serializable {
    private static final long serialVersionUID = 516854665688212949L;

    public interface ShareType {
        int SHARE_GET_MONEY_BEAN = 0;
        int SHARE_GET_MONEY = 1;
        int SHARE_GET_BEAN = 2;
    }

    private int goodsCredits;               //返利数值
    private String shareDescription;        //返利描述
    private String shareUrl;                //跳转链接
    private String shareTypeStr;            //分享类型
    private int shareType;               //0分享返佣+分享立得,1仅分享返佣,2仅分享立得
    private String shareTitle;              //详情页分享标题
    private int showShareUrl;               //是否展示分享链接 0不展示，1展示
    private String ruleUrl;                 //cps分享规则字段
    private String goodsBrokerage;           //分享返利佣金
    private int showGoodsBrokerage;      //是否展示佣金数量 0：不展示 1：展示
    private String floatTitle;           //3.4版本 分享浮层标题

    public void setGoodsCredits(int goodsCredits) {
        this.goodsCredits = goodsCredits;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getGoodsCredits() {
        return goodsCredits;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareTypeStr() {
        return shareTypeStr;
    }

    public void setShareTypeStr(String shareTypeStr) {
        this.shareTypeStr = shareTypeStr;
    }

    public String getRuleUrl() {
        return ruleUrl;
    }

    public void setRuleUrl(String ruleUrl) {
        this.ruleUrl = ruleUrl;
    }

    public String getGoodsBrokerage() {
        return goodsBrokerage;
    }

    public void setGoodsBrokerage(String goodsBrokerage) {
        this.goodsBrokerage = goodsBrokerage;
    }

    public int getShowGoodsBrokerage() {
        return showGoodsBrokerage;
    }

    public void setShowGoodsBrokerage(int showGoodsBrokerage) {
        this.showGoodsBrokerage = showGoodsBrokerage;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public int getShowShareUrl() {
        return showShareUrl;
    }

    public void setShowShareUrl(int showShareUrl) {
        this.showShareUrl = showShareUrl;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getFloatTitle() {
        return floatTitle;
    }

    public void setFloatTitle(String floatTitle) {
        this.floatTitle = floatTitle;
    }
}
