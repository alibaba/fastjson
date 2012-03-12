package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_lenolix_9 extends TestCase {

    public void test_for_objectKey() throws Exception {
        Map<String, Object> submap4 = new HashMap<String, Object>();
        Address address = new Address();
        address.setCity("hangzhou");
        address.setStreet("wangshang.RD");
        address.setPostCode(310002);
        submap4.put("address1", address);
        submap4.put("address2", address);

        String mapString4 = JSON.toJSONString(submap4, SerializerFeature.WriteClassName,
                                              SerializerFeature.WriteMapNullValue);
        
        System.out.println(mapString4);
        Object object4 = JSON.parse(mapString4);
        Assert.assertNotNull(object4);
        
        Map<String, Object> map = (Map<String, Object>) object4;
        
        Assert.assertNotNull(map.get("address1"));
        Assert.assertNotNull(map.get("address2"));

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
