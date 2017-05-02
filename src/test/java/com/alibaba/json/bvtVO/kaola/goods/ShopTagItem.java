package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by bojan on 2016/12/29.
 */

public class ShopTagItem implements Serializable {
    private static final long serialVersionUID = 1406182416550994523L;
    private String tagContent;          //商家服务标内容
    private String tagIconUrl;           //服务标URL

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public String getTagIconUrl() {
        return tagIconUrl;
    }

    public void setTagIconUrl(String tagIconUrl) {
        this.tagIconUrl = tagIconUrl;
    }
}
