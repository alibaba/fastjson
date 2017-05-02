package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by xinyang on 2015/9/6.
 */
public class Brand implements Serializable {

    private static final long serialVersionUID = -2042796441456509240L;
    private long brandId;               //品牌ID
    private String brandLogoUrl;        //品牌LOGO图片
    private String brandName;           //品牌名称
    private String countryName;         //国家名
    private String flagImage;           //国家图片
    private String introduce;           //品牌描述

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(String flagImage) {
        this.flagImage = flagImage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
