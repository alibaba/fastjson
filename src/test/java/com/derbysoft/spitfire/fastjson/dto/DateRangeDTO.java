package com.derbysoft.spitfire.fastjson.dto;

import java.util.Date;

public class DateRangeDTO extends AbstractDTO{
    private Date start;
    private Date end;

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
