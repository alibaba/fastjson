package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_SpitFire_6 extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_SpitFire_6.");
    }

    public void test_ref() throws Exception {
        GenericRS<HotelAvailRS> rs = new GenericRS<HotelAvailRS>();
        HotelAvailRS availRs = new HotelAvailRS();
        AvailRoomStayDTO stay = new AvailRoomStayDTO();
        availRs.getHotelAvailRoomStay().getRoomStays().add(stay);
        availRs.getHotelAvailRoomStay().getRoomStays().add(stay);
        availRs.getHotelAvailRoomStay().getRoomStays().add(stay);
        availRs.getHotelAvailRoomStay().getRoomStays().add(stay);
        rs.setPayload(availRs);

        String text = JSON.toJSONString(rs, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat);
        System.out.println(text);
        
        JSON.parseObject(text, GenericRS.class);
    }

    public static class GenericRS<T> {

        private T payload;

        public T getPayload() {
            return payload;
        }

        public void setPayload(T payload) {
            this.payload = payload;
        }

    }

    public static class HotelAvailRS {

        private HotelAvailRoomStayDTO hotelAvailRoomStay = new HotelAvailRoomStayDTO();

        public HotelAvailRoomStayDTO getHotelAvailRoomStay() {
            return hotelAvailRoomStay;
        }

        public void setHotelAvailRoomStay(HotelAvailRoomStayDTO hotelAvailRoomStay) {
            this.hotelAvailRoomStay = hotelAvailRoomStay;
        }

    }

    public static class HotelAvailRoomStayDTO {

        private List<AvailRoomStayDTO> roomStays = new ArrayList<AvailRoomStayDTO>();

        public List<AvailRoomStayDTO> getRoomStays() {
            return roomStays;
        }

        public void setRoomStays(List<AvailRoomStayDTO> roomStays) {
            this.roomStays = roomStays;
        }

    }

    public static class AvailRoomStayDTO {

    }
}
