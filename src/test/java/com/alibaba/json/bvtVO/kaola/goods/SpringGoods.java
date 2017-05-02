package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

public class SpringGoods extends ListSingleGoods implements Serializable {
    private static final long serialVersionUID = -833832665718580242L;

    public static final int HAVE_COLLECTED = 1;     //已经收藏
    public static final int NOT_COLLECTED = 0;      //未收藏
    public static final int PHONE_PRICE_ONLY = 1;   //手机专享价
    public static final int GOODS_OUT_OF_STOCK = 0; //商品没有库存
    public static final int GOODS_OFF_LINE = 0;     //商品下架

    private long brandId;
    private String brandName;
    private String brandLogoUrl;
    private String activityLabel;
    private String goodsStorageLabel;
    private List<String> appImgUrlList;
    //private String slug;
    private String subTitle;
    private String flagUrl;
    private String source;
    private int islike;
    private long likeCount;
    private int freeTax;
    private int importType;
    private List<GoodsPropertyList> skuGoodsPropertyList;
    private List<SkuList> skuList;
    private List<String> zhengdanRulesStr;
    private String xiangouMap;
    private List<String> appActivityTitleList;
    //private String commentCountStr;
    private List<String> couponStr;
    private String couponUrl;
    private int isFocus;
    private String recommendGoodsLabel;
    private double payAmountLimit;
    private String payAmountLimitInfo;
    private String taxLabel;
    private String singleActivityLabelUrl;
    private String taxExplanation;
    private String activityShowUrl;
    private int isShowCart;
    private NewerGift newerGift;
    private List<GiftGood> gifts;
    private int isTax;
    private int freeTaxMaxAmount;
    private GoodsTimeSalePromotions timeSalePromotions;
    private int isPayTax;
    private String taxTag;
    private long[] activityIdList;
    private MemberGood memberGoods;
    private Not4SaleGoodsItem not4SaleGoodsItem;
    private GoodsPopShopModel popShop;
    private int isNeedCustomerService;
    private String customerServiceContent;
    private Consult consult;

    public Consult getConsult() {
        return consult;
    }

    public void setConsult(Consult consult) {
        this.consult = consult;
    }

    public GoodsPopShopModel getPopShop() {
        return popShop;
    }

    public void setPopShop(GoodsPopShopModel popShop) {
        this.popShop = popShop;
    }

    public String getCustomerServiceContent() {
        return customerServiceContent;
    }

    public void setCustomerServiceContent(String customerServiceContent) {
        this.customerServiceContent = customerServiceContent;
    }

    public int isNeedCustomerService() {
        return isNeedCustomerService;
    }

    public void setIsNeedCustomerService(int needCustomerService) {
        isNeedCustomerService = needCustomerService;
    }

    public MemberGood getMemberGoods() {
        return memberGoods;
    }

    public void setMemberGoods(MemberGood memberGoods) {
        this.memberGoods = memberGoods;
    }

    public SpringGoods() {
        springType = TYPE_GOODS;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public String getActivityLabel() {
        return activityLabel;
    }

    public void setActivityLabel(String activityLabel) {
        this.activityLabel = activityLabel;
    }

    public String getGoodsStorageLabel() {
        return goodsStorageLabel;
    }

    public void setGoodsStorageLabel(String goodsStorageLabel) {
        this.goodsStorageLabel = goodsStorageLabel;
    }

    public List<String> getAppImgUrlList() {
        return appImgUrlList;
    }

    public void setAppImgUrlList(List<String> appImgUrlList) {
        this.appImgUrlList = appImgUrlList;
    }

    //public String getSlug() {
    //    return slug;
    //}
    //
    //public void setSlug(String slug) {
    //    this.slug = slug;
    //}

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public int getFreeTax() {
        return freeTax;
    }

    public void setFreeTax(int freeTax) {
        this.freeTax = freeTax;
    }

    public int getImportType() {
        return importType;
    }

    public void setImportType(int importType) {
        this.importType = importType;
    }

    public List<GoodsPropertyList> getSkuGoodsPropertyList() {
        return skuGoodsPropertyList;
    }

    public void setSkuGoodsPropertyList(List<GoodsPropertyList> skuGoodsPropertyList) {
        this.skuGoodsPropertyList = skuGoodsPropertyList;
    }

    public List<SkuList> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuList> skuList) {
        this.skuList = skuList;
    }

    public List<String> getZhengdanRulesStr() {
        return zhengdanRulesStr;
    }

    public void setZhengdanRulesStr(List<String> zhengdanRulesStr) {
        this.zhengdanRulesStr = zhengdanRulesStr;
    }

    public String getXiangouMap() {
        return xiangouMap;
    }

    public void setXiangouMap(String xiangouMap) {
        this.xiangouMap = xiangouMap;
    }

    public List<String> getAppActivityTitleList() {
        return appActivityTitleList;
    }

    public void setAppActivityTitleList(List<String> appActivityTitleList) {
        this.appActivityTitleList = appActivityTitleList;
    }

    /*public String getCommentCountStr() {
        return commentCountStr;
    }

    public void setCommentCountStr(String commentCountStr) {
        this.commentCountStr = commentCountStr;
    }*/

    public List<String> getCouponStr() {
        return couponStr;
    }

    public void setCouponStr(List<String> couponStr) {
        this.couponStr = couponStr;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public String getRecommendGoodsLabel() {
        return recommendGoodsLabel;
    }

    public void setRecommendGoodsLabel(String recommendGoodsLabel) {
        this.recommendGoodsLabel = recommendGoodsLabel;
    }

    public double getPayAmountLimit() {
        return payAmountLimit;
    }

    public void setPayAmountLimit(double payAmountLimit) {
        this.payAmountLimit = payAmountLimit;
    }

    public String getPayAmountLimitInfo() {
        return payAmountLimitInfo;
    }

    public void setPayAmountLimitInfo(String payAmountLimitInfo) {
        this.payAmountLimitInfo = payAmountLimitInfo;
    }

    public String getTaxLabel() {
        return taxLabel;
    }

    public void setTaxLabel(String taxLabel) {
        this.taxLabel = taxLabel;
    }

    public String getSingleActivityLabelUrl() {
        return singleActivityLabelUrl;
    }

    public void setSingleActivityLabelUrl(String singleActivityLabelUrl) {
        this.singleActivityLabelUrl = singleActivityLabelUrl;
    }

    public String getTaxExplanation() {
        return taxExplanation;
    }

    public void setTaxExplanation(String taxExplanation) {
        this.taxExplanation = taxExplanation;
    }

    public String getActivityShowUrl() {
        return activityShowUrl;
    }

    public void setActivityShowUrl(String activityShowUrl) {
        this.activityShowUrl = activityShowUrl;
    }

    public int getIsShowCart() {
        return isShowCart;
    }

    public void setIsShowCart(int isShowCart) {
        this.isShowCart = isShowCart;
    }

    public NewerGift getNewerGift() {
        return newerGift;
    }

    public void setNewerGift(NewerGift newerGift) {
        this.newerGift = newerGift;
    }

    public List<GiftGood> getGifts() {
        return gifts;
    }

    public void setGifts(List<GiftGood> gifts) {
        this.gifts = gifts;
    }

    public int getIsTax() {
        return isTax;
    }

    public void setIsTax(int isTax) {
        this.isTax = isTax;
    }

    public int getFreeTaxMaxAmount() {
        return freeTaxMaxAmount;
    }

    public void setFreeTaxMaxAmount(int freeTaxMaxAmount) {
        this.freeTaxMaxAmount = freeTaxMaxAmount;
    }

    public GoodsTimeSalePromotions getTimeSalePromotions() {
        return timeSalePromotions;
    }

    public void setTimeSalePromotions(GoodsTimeSalePromotions timeSalePromotions) {
        this.timeSalePromotions = timeSalePromotions;
    }

    public String getTaxTag() {
        return taxTag;
    }

    public void setTaxTag(String taxTag) {
        this.taxTag = taxTag;
    }

    public int getIsPayTax() {
        return isPayTax;
    }

    public void setIsPayTax(int isPayTax) {
        this.isPayTax = isPayTax;
    }

    public long[] getActivityIdList() {
        return activityIdList;
    }

    public void setActivityIdList(long[] activityIdList) {
        this.activityIdList = activityIdList;
    }

    public Not4SaleGoodsItem getNot4SaleGoodsItem() {
        return not4SaleGoodsItem;
    }

    public void setNot4SaleGoodsItem(Not4SaleGoodsItem not4SaleGoodsItem) {
        this.not4SaleGoodsItem = not4SaleGoodsItem;
    }
}
