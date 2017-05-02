package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by hzfengboyang on 2016/5/3.
 */
public class GoodsActivityItemGoods implements Serializable {

    private static final long serialVersionUID = -1015984430269297844L;
    private String imgUrl;      //图片链接
    private int actualStore;  // 库存，0缺货，>1 不缺货
    private long goodsId;     //商品id
    private String freeNumber;  //赠送数量

    public int getActualStore() {
        return actualStore;
    }

    public void setActualStore(int actualStore) {
        this.actualStore = actualStore;
    }

    public String getFreeNumber() {
        return freeNumber;
    }

    public void setFreeNumber(String freeNumber) {
        this.freeNumber = freeNumber;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
