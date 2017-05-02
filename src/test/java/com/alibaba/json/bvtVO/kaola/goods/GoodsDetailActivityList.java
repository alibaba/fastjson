package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinyang on 2015/11/23.
 */
public class GoodsDetailActivityList implements Serializable {
    private static final long serialVersionUID = -2918366643311279259L;

    private String title;
    private List<ActivityListItem> activityList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ActivityListItem> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityListItem> activityList) {
        this.activityList = activityList;
    }
}
