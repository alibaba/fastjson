package com.alibaba.fastjson.deserializer.issue4129.beans;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Policy {

    @JSONField(format = "yyyyMMddHHmmss.SSS")
    private Date startTime;

    @JSONField(format = "yyyyMMddHHmmssSSS")
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
