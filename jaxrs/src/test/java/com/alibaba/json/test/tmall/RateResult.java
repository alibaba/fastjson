package com.alibaba.json.test.tmall;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * User: jingxian.lzg
 * Date: 2015/9/1
 * Time: 14:51
 */
public class RateResult {
    @JSONField(name = "head")
    private Head head;

    @JSONField(name = "auctions")
    private List<RateSearchItemDO> comments;

    @JSONField(name = "statistics")
    private String stats;

    public Head getHead () {
        return head;
    }

    public void setHead (Head head) {
        this.head = head;
    }

    public List<RateSearchItemDO> getComments () {
        return comments;
    }

    public void setComments (List<RateSearchItemDO> comments) {
        this.comments = comments;
    }

    public String getStats () {
        return stats;
    }

    public void setStats (String stats) {
        this.stats = stats;
    }
}
