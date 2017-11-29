package com.alibaba.json.bvt.parser.deser.generic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class GenericTest4 extends TestCase {

    public void test_0() throws Exception {
        String text;
        {
            User user = new User("Z友群");
            user.getAddresses().add(new Address("滨江"));
            text = JSON.toJSONString(user);
        }
        
        System.out.println(text);
        
        User user = JSON.parseObject(text, User.class);
        
        Assert.assertEquals("Z友群", user.getName());
        Assert.assertEquals(1, user.getAddresses().size());
        Assert.assertEquals(Address.class, user.getAddresses().get(0).getClass());
        Assert.assertEquals("滨江", user.getAddresses().get(0).getValue());
    }

    public static class User {

        private String name;

        public User(){

        }

        public User(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private List<Address> addresses = new ArrayList<Address>();

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
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
