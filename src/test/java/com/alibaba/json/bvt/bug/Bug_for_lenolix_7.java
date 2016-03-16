package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.Date;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.parser.GenericTest.Address;

public class Bug_for_lenolix_7 extends TestCase {

    public void test_for_objectKey() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("leno.lix");
        user.setIsBoy(true);
        user.setBirthDay(new Date());
        user.setGmtCreate(new java.util.Date(new Date().getTime()));
        user.setGmtModified(new java.util.Date(new Date().getTime()));
        String userJSON = JSON.toJSONString(user, SerializerFeature.WriteClassName,
                                            SerializerFeature.WriteMapNullValue);

        System.out.println(userJSON);

        User returnUser = (User) JSON.parse(userJSON);

    }

    public static class User implements Serializable {

        /**
             *
             */

        private static final long serialVersionUID = 6192533820796587011L;

        private Integer           id;
        private String            name;
        private Boolean           isBoy;
        private Address           address;
        private Date              birthDay;
        private java.util.Date    gmtCreate;
        private java.util.Date    gmtModified;

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

        public java.util.Date getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(java.util.Date gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public java.util.Date getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(java.util.Date gmtModified) {
            this.gmtModified = gmtModified;
        }

    }
}
