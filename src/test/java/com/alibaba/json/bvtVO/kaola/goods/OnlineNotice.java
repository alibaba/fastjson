package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by xinyang on 2015/10/20.
 */
public class OnlineNotice implements Serializable {

    private static final long serialVersionUID = 1654592510075752360L;


    /**
     * backColor : #8B008B
     * closeType : 0
     * contentType : 1
     * createTime : 1432121873000
     * endTime : 1451475456000
     * id : 2015052019NOTE57571466
     * noticeType : 22
     * showType : 1
     * siteUrl : http://m.kaola.com/activity/spring/h5/3394.html
     * startTime : 1445254656000
     * status : 1
     * text : 凌乱凌乱~~~~~~商品通知通知~~~~~~~~~~~~
     * updateTime : 1445309794000
     */

    private String backColor;
    private int contentType;
    private String siteUrl;
    private String text;
    private String imageUrl;
    private String dotName;

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDotName() {
        return dotName;
    }

    public void setDotName(String dotName) {
        this.dotName = dotName;
    }
}
