package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情页评价
 * Created by xinyang on 2015/9/10.
 */
public class GoodsDetailComment implements Serializable {

    private static final long serialVersionUID = 3806331344610054683L;
    private String commentLabel;
    private List<GoodsComment> excellentCommentList;

    public String getCommentLabel() {
        return commentLabel;
    }

    public void setCommentLabel(String commentLabel) {
        this.commentLabel = commentLabel;
    }

    public List<GoodsComment> getExcellentCommentList() {
        return excellentCommentList;
    }

    public void setExcellentCommentList(List<GoodsComment> excellentCommentList) {
        this.excellentCommentList = excellentCommentList;
    }
}
