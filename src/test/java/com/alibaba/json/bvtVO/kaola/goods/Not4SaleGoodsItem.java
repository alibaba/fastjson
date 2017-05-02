package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

public class Not4SaleGoodsItem implements Serializable {

    private static final long serialVersionUID = -7254569435143277161L;
    private String buttonUrl;               //按钮跳转的url
    private String buttonContent;           //商品详情页非卖品按钮文案，如“本商品不支持购买”
    private int isNot4SaleGoods;            //是否为非卖品，0表示否，1表示是
    private String buttonBackColor;         //按钮的底色
    private String buttonContentColor;      //按钮的文字颜色

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public String getButtonContent() {
        return buttonContent;
    }

    public void setButtonContent(String buttonContent) {
        this.buttonContent = buttonContent;
    }

    public int getIsNot4SaleGoods() {
        return isNot4SaleGoods;
    }

    public void setIsNot4SaleGoods(int isNot4SaleGoods) {
        this.isNot4SaleGoods = isNot4SaleGoods;
    }

    public String getButtonBackColor() {
        return buttonBackColor;
    }

    public void setButtonBackColor(String buttonBackColor) {
        this.buttonBackColor = buttonBackColor;
    }

    public String getButtonContentColor() {
        return buttonContentColor;
    }

    public void setButtonContentColor(String buttonContentColor) {
        this.buttonContentColor = buttonContentColor;
    }
}
