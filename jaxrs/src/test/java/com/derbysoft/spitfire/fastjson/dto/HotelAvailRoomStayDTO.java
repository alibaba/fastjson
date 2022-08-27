package com.derbysoft.spitfire.fastjson.dto;

import java.util.ArrayList;
import java.util.List;

public class HotelAvailRoomStayDTO extends AbstractDTO{


    private List<AvailRoomStayDTO> roomStays = new ArrayList<AvailRoomStayDTO>();

    private HotelRefDTO hotelRef;

    private TPAExtensionsDTO tpaExtensions;

    public List<AvailRoomStayDTO> getRoomStays() {
        return roomStays;
    }

    public void setRoomStays(List<AvailRoomStayDTO> roomStays) {
        this.roomStays = roomStays;
    }

    public HotelRefDTO getHotelRef() {
        return hotelRef;
    }

    public void setHotelRef(HotelRefDTO hotelRef) {
        this.hotelRef = hotelRef;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
