package com.derbysoft.spitfire.fastjson.dto;

public class HotelAvailRS extends AbstractRS {
    private HotelAvailRoomStayDTO hotelAvailRoomStay;

    private TPAExtensionsDTO tpaExtensions;

    public HotelAvailRoomStayDTO getHotelAvailRoomStay() {
        return hotelAvailRoomStay;
    }

    public void setHotelAvailRoomStay(HotelAvailRoomStayDTO hotelAvailRoomStay) {
        this.hotelAvailRoomStay = hotelAvailRoomStay;
    }

    public TPAExtensionsDTO getTpaExtensions() {
        return tpaExtensions;
    }

    public void setTpaExtensions(TPAExtensionsDTO tpaExtensions) {
        this.tpaExtensions = tpaExtensions;
    }
}
