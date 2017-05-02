package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by xinyang on 2015/9/6.
 */
public class NewerGift implements Serializable {

    private int isNewerGift;                //是否是新人礼
    private String newerGiftButton;         //按钮上的文字
    private String newerGiftUrl;            //新人礼的跳转连接

    public int getIsNewerGift() {
        return isNewerGift;
    }

    public void setIsNewerGift(int isNewerGift) {
        this.isNewerGift = isNewerGift;
    }

    public String getNewerGiftButton() {
        return newerGiftButton;
    }

    public void setNewerGiftButton(String newerGiftButton) {
        this.newerGiftButton = newerGiftButton;
    }

    public String getNewerGiftUrl() {
        return newerGiftUrl;
    }

    public void setNewerGiftUrl(String newerGiftUrl) {
        this.newerGiftUrl = newerGiftUrl;
    }
}
