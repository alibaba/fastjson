package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class Bug13 extends TestCase {
    public void test_0() throws Exception {
        User user = new User("name1", "11");  
        String object = JSON.toJSONString(user);  
        System.out.println(object);  
        user = JSON.parseObject(object, User.class);//报错  
    }
    
    public static class User {  
        public User() {  
        }  
      
        private String name, age;  
        private List<Object> group = new ArrayList<Object>(2);  
      
        public List<Object> getGroup() {  
            return group;  
        }  
      
        public void setGroup(List<Object> group) {  
            this.group = group;  
        }  
      
        public String getName() {  
            return name;  
        }  
      
        public void setName(String name) {  
            this.name = name;  
        }  
      
        public String getAge() {  
            return age;  
        }  
      
        public void setAge(String age) {  
            this.age = age;  
        }  
      
        public User(String name, String age) {  
            this.name = name;  
            this.age = age;  
            group.add("1");  
            group.add("2");  
        }  
    }  
}
