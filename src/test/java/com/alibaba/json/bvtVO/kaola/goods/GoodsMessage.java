package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by xinyang on 2015/9/10.
 */
public class GoodsMessage implements Serializable {

    private String offlineMessage;
    private String soldOutMessage;

    public String getOfflineMessage() {
        return offlineMessage;
    }

    public void setOfflineMessage(String offlineMessage) {
        this.offlineMessage = offlineMessage;
    }

    public String getSoldOutMessage() {
        return soldOutMessage;
    }

    public void setSoldOutMessage(String soldOutMessage) {
        this.soldOutMessage = soldOutMessage;
    }
}
