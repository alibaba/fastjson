package com.derbysoft.spitfire.fastjson.dto;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class RoomStayCandidateDTO extends AbstractDTO{
    private int numberOfUnits;
    private List<GuestCountDTO> guests = new ArrayList<GuestCountDTO>();

    public RoomStayCandidateDTO() {
    }

    public RoomStayCandidateDTO(int numberOfUnits, List<GuestCountDTO> guests) {
        this.numberOfUnits = numberOfUnits;
        this.guests = guests;
    }

    @JSONField(name="Unit")
    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    @JSONField(name="Unit")
    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public List<GuestCountDTO> getGuests() {
        return guests;
    }

    public void setGuests(List<GuestCountDTO> guests) {
        this.guests = guests;
    }
}
