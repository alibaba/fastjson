package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hzfengboyang on 2016/5/3.
 */
public class GoodsActivityPromotionDetail implements Serializable {

    private static final long serialVersionUID = 4827851349919648800L;
    private String title;
    private String content;
    private String link;
    private List<GoodsActivityItemGoods> goods;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GoodsActivityItemGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsActivityItemGoods> goods) {
        this.goods = goods;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
