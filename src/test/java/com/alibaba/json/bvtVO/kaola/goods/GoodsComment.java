package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * 商品评价
 *
 * @author xinyang
 * @time 2015/1/16.
 */
public class GoodsComment implements Serializable {
    private static final long serialVersionUID = 7712858080288005485L;
    private List<String> allFeatures;       //全部印象
    private Goods goods;                    //商品信息
    private List<SkuProperty> skuPropertyList;
    private String goodsCommentId;          //商品是否已经评价过(为空没评价过)
    private String canAppend;               //商品是否可以追加评价
    private String commentPoint;            //商品是否有评分
    private String commentFeatures;         //已经评价过得标签
    private String commentContent;          //评价内容
    private String skuId;
    private String goodsId;
    private String createTime;              //评价日期
    private List<String> imgUrls;
    private List<GoodsCommentReply> replyList;
    private String nicknameKaola;           //昵称
    private String avatarKaola;             //头像
    private int commentStatus;
    private int userRegisterDay;
    private int zanCount;
    private boolean zanStatus;
    private int excellentImgCnt;           //精华评论的图片张数
    private int trialReportStatus;          //试用报告 0不是 1是

    public GoodsComment() {

    }

    public int getExcellentImgCnt() {
        return excellentImgCnt;
    }

    public void setExcellentImgCnt(int excellentImgCnt) {
        this.excellentImgCnt = excellentImgCnt;
    }

    public int getTrialReportStatus() {
        return trialReportStatus;
    }

    public void setTrialReportStatus(int trialReportStatus) {
        this.trialReportStatus = trialReportStatus;
    }

    public List<String> getAllFeatures() {
        return allFeatures;
    }

    public void setAllFeatures(List<String> allFeatures) {
        this.allFeatures = allFeatures;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public String getGoodsCommentId() {
        return goodsCommentId;
    }

    public void setGoodsCommentId(String goodsCommentId) {
        this.goodsCommentId = goodsCommentId;
    }

    public String getCanAppend() {
        return canAppend;
    }

    public void setCanAppend(String canAppend) {
        this.canAppend = canAppend;
    }

    public String getCommentPoint() {
        return commentPoint;
    }

    public void setCommentPoint(String commentPoint) {
        this.commentPoint = commentPoint;
    }

    public String getCommentFeatures() {
        return commentFeatures;
    }

    public void setCommentFeatures(String commentFeatures) {
        this.commentFeatures = commentFeatures;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getCreateTime() {
        String createDate = createTime.split(" ")[0];//返回日期
        return createDate;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<SkuProperty> getSkuPropertyList() {
        return skuPropertyList;
    }

    public void setSkuPropertyList(List<SkuProperty> skuPropertyList) {
        this.skuPropertyList = skuPropertyList;
    }

    public List<GoodsCommentReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<GoodsCommentReply> replyList) {
        this.replyList = replyList;
    }

    public String getAvatarKaola() {
        return avatarKaola;
    }

    public void setAvatarKaola(String avatarKaola) {
        this.avatarKaola = avatarKaola;
    }

    public String getNicknameKaola() {
        return nicknameKaola;
    }

    public void setNicknameKaola(String nicknameKaola) {
        this.nicknameKaola = nicknameKaola;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getUserRegisterDay() {
        return userRegisterDay;
    }

    public void setUserRegisterDay(int userRegisterDay) {
        this.userRegisterDay = userRegisterDay;
    }

    public int getZanCount() {
        return zanCount;
    }

    public void setZanCount(int zanCount) {
        this.zanCount = zanCount;
    }

    public boolean getZanStatus() {
        return zanStatus;
    }

    public void setZanStatus(boolean zanStatus) {
        this.zanStatus = zanStatus;
    }
}
