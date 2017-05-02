package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinyang on 2015/11/16.
 */
public class GoodsDetail extends SpringGoods implements Serializable {

    private static final long serialVersionUID = 9216579832150500900L;
    private List<GoodsPropertyList> goodsPropertyList;//商品所有属性
    private Brand brand;                        //详情页品牌入口
    private long commentCount;                  //评价数量
    private String faqLabel;                    // 显示直邮FAQ、保税区FAQ
    private GoodsMessage goodsMessage;          //缺货和下架提示
    private GoodsDetailComment comment;         //精华评价显示
    private int popWindowSwitch;                //弹幕开关
    private OnlineNotice onlineNotice;          //通知
    private int preSale;                        //0:不是预售商品 默认 1:是预售商品
    private String preSaleDesc;                 //预售商品描述
    private List<GoodsDetailActivityList> activityList;//商品详情页活动
    private GoodsDetailCps cps;                 //分享返利
    private GoodsIllustrate illustrate;         //商品说明
    private GoodsColorSelection colorSelection; //色卡中的图片
    private int showKaolaAnswersGuide;          //是否显示小考拉答疑的引导，0不展示，1展示
    private GoodsDetailAlert alert;             //详情页弹窗
    private String goodsDetailUrl;              //图文详情的地址
    private String promotion;                   //首屏促销信息，html格式的
    private String promotionUrl;                //3.3 非会员商品跳转会员商品的Url
    private int kaolaAnswersShowed;              //小考拉答疑是否出现了，0没出现，1出现了
    private List<GoodsTag> goodsTags;            //商品标签
    private GoodsActivityPromotion activityPromotion;  //商品详情页的促销信息
    private GoodsDelivery delivery;              //次日达及配送信息
    private String warehouse;                    //发货仓库
    private int showDelivery;                   //是否显示配送模块，0不显示，1显示
    private List<String> bannerImgUrlList;       //详情页轮播图
    private List<String> priceTags;               //3.1版本 价格旁边的标签
    private int supportKaolaBean;               //是否支持考拉豆, 1支持,0不支持
    private int showDetailIcon;                    //3.1版本 是否显示图文详情
    private UrlConfigItem urlConfig;                //商品详情中相关的url
    private String goodsRankingUrl;              //3.3版本排行榜标的跳转URL
    private int showGoodsRankingIcon;               //3.3版本是否显示排行榜图标
    private String goodsRankingIconUrl;             //3.3版本排行榜按钮的url
    private int rankTabFirst;                  //3.3版本tab中的排行榜是否是第一个 1是 0不是
    private String faqUrl;                      //faq链接
    private String shareImageUrl;               //3.4版本商品详情页分享的图片
    private String shareSubContent;             //3.4版本详情页分享副文案
    private String shareIntroduce;              //3.4版本详情页分享十字描述
    private List<VirtualGoodsView> virtualGoodsList;    //APP3.5 成分商品对应的虚拟商品列表

    public List<VirtualGoodsView> getVirtualGoodsList() {
        return virtualGoodsList;
    }

    public void setVirtualGoodsList(List<VirtualGoodsView> virtualGoodsList) {
        this.virtualGoodsList = virtualGoodsList;
    }

    public String getShareIntroduce() {
        return shareIntroduce;
    }

    public void setShareIntroduce(String shareIntroduce) {
        this.shareIntroduce = shareIntroduce;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    public String getShareSubContent() {
        return shareSubContent;
    }

    public void setShareSubContent(String shareSubContent) {
        this.shareSubContent = shareSubContent;
    }

    public int getRankTabFirst() {
        return rankTabFirst;
    }

    public void setRankTabFirst(int rankTabFirst) {
        this.rankTabFirst = rankTabFirst;
    }

    public String getGoodsRankingUrl() {
        return goodsRankingUrl;
    }

    public void setGoodsRankingUrl(String goodsRankingUrl) {
        this.goodsRankingUrl = goodsRankingUrl;
    }

    public int getShowGoodsRankingIcon() {
        return showGoodsRankingIcon;
    }

    public void setShowGoodsRankingIcon(int showGoodsRankingIcon) {
        this.showGoodsRankingIcon = showGoodsRankingIcon;
    }

    public String getGoodsRankingIconUrl() {
        return goodsRankingIconUrl;
    }

    public void setGoodsRankingIconUrl(String goodsRankingIconUrl) {
        this.goodsRankingIconUrl = goodsRankingIconUrl;
    }

    public String getPromotionUrl() {
        return promotionUrl;
    }

    public void setPromotionUrl(String promotionUrl) {
        this.promotionUrl = promotionUrl;
    }

    public UrlConfigItem getUrlConfig() {
        return urlConfig;
    }

    public void setUrlConfig(UrlConfigItem urlConfig) {
        this.urlConfig = urlConfig;
    }

    public int getShowDetailIcon() {
        return showDetailIcon;
    }

    public void setShowDetailIcon(int showDetailIcon) {
        this.showDetailIcon = showDetailIcon;
    }

    public List<String> getPriceTags() {
        return priceTags;
    }

    public void setPriceTags(List<String> priceTags) {
        this.priceTags = priceTags;
    }

    public List<String> getBannerImgUrlList() {
        return bannerImgUrlList;
    }

    public void setBannerImgUrlList(List<String> bannerImgUrlList) {
        this.bannerImgUrlList = bannerImgUrlList;
    }

    public String getGoodsDetailUrl() {
        return goodsDetailUrl;
    }

    public void setGoodsDetailUrl(String goodsDetailUrl) {
        this.goodsDetailUrl = goodsDetailUrl;
    }

    public int getShowDelivery() {
        return showDelivery;
    }

    public void setShowDelivery(int showDelivery) {
        this.showDelivery = showDelivery;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public GoodsDelivery getDelivery() {
        return delivery;
    }

    public void setDelivery(GoodsDelivery delivery) {
        this.delivery = delivery;
    }

    public GoodsActivityPromotion getActivityPromotion() {
        return activityPromotion;
    }

    public void setActivityPromotion(GoodsActivityPromotion activityPromotion) {
        this.activityPromotion = activityPromotion;
    }

    public List<GoodsTag> getGoodsTags() {
        return goodsTags;
    }

    public void setGoodsTags(List<GoodsTag> goodsTags) {
        this.goodsTags = goodsTags;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public List<GoodsPropertyList> getGoodsPropertyList() {
        return goodsPropertyList;
    }

    public void setGoodsPropertyList(List<GoodsPropertyList> goodsPropertyList) {
        this.goodsPropertyList = goodsPropertyList;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getFaqLabel() {
        return faqLabel;
    }

    public void setFaqLabel(String faqLabel) {
        this.faqLabel = faqLabel;
    }

    public GoodsMessage getGoodsMessage() {
        return goodsMessage;
    }

    public void setGoodsMessage(GoodsMessage goodsMessage) {
        this.goodsMessage = goodsMessage;
    }

    public GoodsDetailComment getComment() {
        return comment;
    }

    public void setComment(GoodsDetailComment comment) {
        this.comment = comment;
    }

    public int getPopWindowSwitch() {
        return popWindowSwitch;
    }

    public void setPopWindowSwitch(int popWindowSwitch) {
        this.popWindowSwitch = popWindowSwitch;
    }

    public OnlineNotice getOnlineNotice() {
        return onlineNotice;
    }

    public void setOnlineNotice(OnlineNotice onlineNotice) {
        this.onlineNotice = onlineNotice;
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

    public List<GoodsDetailActivityList> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<GoodsDetailActivityList> activityList) {
        this.activityList = activityList;
    }

    public GoodsDetailCps getCps() {
        return cps;
    }

    public void setCps(GoodsDetailCps cps) {
        this.cps = cps;
    }

    public GoodsIllustrate getIllustrate() {
        return illustrate;
    }

    public void setIllustrate(GoodsIllustrate illustrate) {
        this.illustrate = illustrate;
    }

    public GoodsColorSelection getColorSelection() {
        return colorSelection;
    }

    public void setColorSelection(GoodsColorSelection colorSelection) {
        this.colorSelection = colorSelection;
    }

    public int getShowKaolaAnswersGuide() {
        return showKaolaAnswersGuide;
    }

    public void setShowKaolaAnswersGuide(int showKaolaAnswersGuide) {
        this.showKaolaAnswersGuide = showKaolaAnswersGuide;
    }

    public int getKaolaAnswersShowed() {
        return kaolaAnswersShowed;
    }

    public void setKaolaAnswersShowed(int kaolaAnswersShowed) {
        this.kaolaAnswersShowed = kaolaAnswersShowed;
    }

    public GoodsDetailAlert getAlert() {
        return alert;
    }

    public void setAlert(GoodsDetailAlert alert) {
        this.alert = alert;
    }

    public int getSupportKaolaBean() {
        return supportKaolaBean;
    }

    public void setSupportKaolaBean(int supportKaolaBean) {
        this.supportKaolaBean = supportKaolaBean;
    }

    public String getFaqUrl() {
        return faqUrl;
    }

    public void setFaqUrl(String faqUrl) {
        this.faqUrl = faqUrl;
    }
}
