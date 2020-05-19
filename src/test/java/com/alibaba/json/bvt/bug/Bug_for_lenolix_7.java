package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_lenolix_7 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;

        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_lenolix_7");
    }
    
    public void test_for_objectKey() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("leno.lix");
        user.setIsBoy(true);
        user.setBirthDay(new Date());
        user.setGmtCreate(new java.sql.Date(new Date().getTime()));
        user.setGmtModified(new java.sql.Timestamp(new Date().getTime()));
        String userJSON = JSON.toJSONString(user, SerializerFeature.WriteClassName, SerializerFeature.WriteMapNullValue);

        System.out.println(userJSON);

        User returnUser = (User) JSON.parse(userJSON);

    }

    private static class User implements Serializable {

        /**
             *
             */

        private static final long  serialVersionUID = 6192533820796587011L;

        private Integer            id;
        private String             name;
        private Boolean            isBoy;
        private Address            address;
        private Date               birthDay;
        private java.sql.Date      gmtCreate;
        private java.sql.Timestamp gmtModified;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getIsBoy() {
            return isBoy;
        }

        public void setIsBoy(Boolean isBoy) {
            this.isBoy = isBoy;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Date getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(Date birthDay) {
            this.birthDay = birthDay;
        }

        public java.sql.Date getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(java.sql.Date gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public java.sql.Timestamp getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(java.sql.Timestamp gmtModified) {
            this.gmtModified = gmtModified;
        }

    }
    
    public static class Address {

        private String value;

        public Address(){
        }

        public Address(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
