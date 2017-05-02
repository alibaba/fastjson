package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzfengboyang on 2016/5/3.
 */
public class GoodsActivityPromotion implements Serializable {

    private static final long serialVersionUID = 5899773930210296918L;
    private String title;                                       //标题
    private List <String> contents;                             //内容列表
    private String detailTitle;                                 //详情的标题
    private List <GoodsActivityPromotionDetail> detailContents; //详情的条目
    private List<GoodsActivityItemGoods>  goods;                 //3.1版本赠品

    public List<GoodsActivityItemGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsActivityItemGoods> goods) {
        this.goods = goods;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GoodsActivityPromotionDetail> getDetailContents() {
        return detailContents;
    }

    public void setDetailContents(List<GoodsActivityPromotionDetail> detailContents) {
        this.detailContents = detailContents;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

}
