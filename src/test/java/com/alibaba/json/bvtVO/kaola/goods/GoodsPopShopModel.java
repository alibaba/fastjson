package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

public class GoodsPopShopModel implements Serializable {
    private static final long serialVersionUID = -5123357868281986838L;

    private String shopUrl;
    private String shopLogo;
    private String shopName;
    private String shopDesc;
    private String shopTagUrl;
    private String shopId;
    private long merchantId;
    private int shopTag;
    private List<ShopTagItem> shopTagList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }
    public int getShopTag() {
        return shopTag;
    }

    public void setShopTag(int shopTag) {
        this.shopTag = shopTag;
    }

    public String getShopTagUrl() {
        return shopTagUrl;
    }

    public void setShopTagUrl(String shopTagUrl) {
        this.shopTagUrl = shopTagUrl;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public List<ShopTagItem> getShopTagList() {
        return shopTagList;
    }

    public void setShopTagList(List<ShopTagItem> shopTagList) {
        this.shopTagList = shopTagList;
    }
}
