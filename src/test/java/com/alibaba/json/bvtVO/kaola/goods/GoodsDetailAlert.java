package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by xinyang on 2016/3/21.
 */
public class GoodsDetailAlert implements Serializable{
    private static final long serialVersionUID = 2827364494305883018L;

    private String button;
    private String title;
    private String content;

    public void setButton(String button) {
        this.button = button;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getButton() {
        return button;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
