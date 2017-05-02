package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by bojan on 2016/12/29.
 */

public class VirtualGoodsView implements Serializable {
    private static final long serialVersionUID = 5544151011939425373L;
    private String buttonContent;    //按钮文案
    private String buttonUrl;         // 按钮跳转url
    private String virtualGoodsId;   //虚拟组合商品的id

    public String getButtonContent() {
        return buttonContent;
    }

    public void setButtonContent(String buttonContent) {
        this.buttonContent = buttonContent;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public String getVirtualGoodsId() {
        return virtualGoodsId;
    }

    public void setVirtualGoodsId(String virtualGoodsId) {
        this.virtualGoodsId = virtualGoodsId;
    }
 }

