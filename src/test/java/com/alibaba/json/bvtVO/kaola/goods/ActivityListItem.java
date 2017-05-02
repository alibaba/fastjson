package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinyang on 2015/11/23.
 */
public class ActivityListItem implements Serializable {
    private static final long serialVersionUID = 3582980766967036213L;

    private String activityTitle;
    private String activityUrl;
    private List<ActivityGoodsImage> goods;

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public List<ActivityGoodsImage> getGoods() {
        return goods;
    }

    public void setGoods(List<ActivityGoodsImage> goods) {
        this.goods = goods;
    }

    public static class ActivityGoodsImage implements Serializable {

        private static final long serialVersionUID = 5406820919987105380L;
        private String imgUrl;
        private int actualStore;

        public int getActualStore() {
            return actualStore;
        }

        public void setActualStore(int actualStore) {
            this.actualStore = actualStore;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
