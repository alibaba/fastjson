package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by bojan on 2016/7/28.
 */

public class UrlConfigItem implements Serializable {
    private static final long serialVersionUID = 8251011856717879983L;
    private String videoUrl;             //   视频url
    private int  startFrame;            //  3.1版本 开始的帧数
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }
}
