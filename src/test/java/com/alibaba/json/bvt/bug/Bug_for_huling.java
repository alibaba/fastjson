package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_huling extends TestCase {

    public void test_for_objectKey() throws Exception {
        String text = "\0\0";
        System.out.println("A" + JSON.toJSONString(text) + "B");

    }

    public static class Address {

        private String city;
        private String street;
        private int postCode;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public int getPostCode() {
            return postCode;
        }

        public void setPostCode(int postCode) {
            this.postCode = postCode;
        }

    }
}
