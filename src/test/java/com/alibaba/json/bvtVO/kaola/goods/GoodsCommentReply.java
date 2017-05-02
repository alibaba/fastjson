package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinyang on 2015/10/21.
 */
public class GoodsCommentReply implements Serializable {

    private static final long serialVersionUID = 2876510594038969078L;
    private int afterDays;
    private List<String> imgUrls;
    private String replyContent;
    private int replyType;
    private List<GoodsCommentReply> replysList;

    public int getAfterDays() {
        return afterDays;
    }

    public void setAfterDays(int afterDays) {
        this.afterDays = afterDays;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public int getReplyType() {
        return replyType;
    }

    public void setReplyType(int replyType) {
        this.replyType = replyType;
    }

    public List<GoodsCommentReply> getReplysList() {
        return replysList;
    }

    public void setReplysList(List<GoodsCommentReply> replysList) {
        this.replysList = replysList;
    }
}
