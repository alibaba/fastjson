package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzfengboyang on 2016/3/1.
 */
public class GoodsBrand implements Serializable {
    private static final long serialVersionUID = 6312819512791218985L;
    private long mBrandId;               //品牌ID
    private String mBrandLogoUrl;        //品牌LOGO图片
    private String mBrandName;           //品牌名称
    private String mGoodsNum;            //在售商品数
    private String mIntroduce;           //品牌描述
    private List<ListSingleGoods> goods; //在售商品

    public String getBrandLogoUrl() {
        return mBrandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.mBrandLogoUrl = brandLogoUrl;
    }

    public long getBrandId() {
        return mBrandId;
    }

    public void setBrandId(long brandId) {
        this.mBrandId = brandId;
    }

    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String brandName) {
        this.mBrandName = brandName;
    }

    public String getGoodsNum() {
        return mGoodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.mGoodsNum = goodsNum;
    }

    public String getIntroduce() {
        return mIntroduce;
    }

    public void setIntroduce(String introduce) {
        this.mIntroduce = introduce;
    }

    public List<ListSingleGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<ListSingleGoods> goods) {
        this.goods = goods;
    }
}
