package com.alibaba.fastjson.deserializer.issue4129.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.time.LocalDateTime;

/**
 * Created on 2022/5/27 15:02
 *
 * @author: wulinfeng
 */
public class Campaign {

    @JSONField(format = "yyyyMMddHHmmss.SSS")
    private LocalDateTime startTime;

    @JSONField(format = "yyyyMMddHHmmss")
    private LocalDateTime endTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
