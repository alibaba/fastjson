package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * 商品说明
 * Created by xinyang on 2015/12/11.
 */
public class GoodsIllustrate implements Serializable {

    private static final long serialVersionUID = -8529448646695618709L;

    private String title;               //标题
    private String detailTitle;         //浮层标题
    private List<String> contents;      //内容列表
    private List<DetailContentsEntity> detailContents;      //浮层中详细内容列表

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public void setDetailContents(List<DetailContentsEntity> detailContents) {
        this.detailContents = detailContents;
    }

    public String getTitle() {
        return title;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public List<String> getContents() {
        return contents;
    }

    public List<DetailContentsEntity> getDetailContents() {
        return detailContents;
    }

    public static class DetailContentsEntity implements Serializable {

        private static final long serialVersionUID = 6194454542085888954L;
        private String title;
        private String content;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String description) {
            this.content = description;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}
