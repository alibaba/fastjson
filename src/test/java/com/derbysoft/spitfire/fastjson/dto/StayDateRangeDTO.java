package com.derbysoft.spitfire.fastjson.dto;

import java.util.Date;

public class StayDateRangeDTO extends AbstractDTO {
    private Date checkInDate;
    private Date checkOutDate;

    public StayDateRangeDTO() {
    }

    public StayDateRangeDTO(Date checkInDate, Date checkOutDate) {
         this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}
