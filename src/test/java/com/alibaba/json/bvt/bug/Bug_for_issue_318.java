package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_318 extends TestCase {

    public void test_for_issue() throws Exception {
        Person o1 = new Person("zhangsan", 20);
        Person o2 = new Person("liuXX", 30);
        Person o3 = new Person("Test", 10);

        List<Person> users = new ArrayList<Person>();
        users.add(o1);
        users.add(o2);
        users.add(o3);

        List<Person> managers = new ArrayList<Person>();
        managers.add(o2);
        managers.add(o3);

        PersonAll pa = new PersonAll();
        pa.setCount(30);

        // map
        Map<String, List<Person>> userMap = new LinkedHashMap<String, List<Person>>();
        userMap.put("managers", managers);
        userMap.put("users", users);
        pa.setUserMap(userMap);
        // bean的属性
        pa.setUsers(users);
        pa.setManagers(managers);

        // String json = JSON.toJSONString(pa, SerializerFeature.DisableCircularReferenceDetect);
        String json = JSON.toJSONString(pa);
//        System.out.println("序列化: ");
//        System.out.println(json);

        PersonAll target = JSON.parseObject(json, PersonAll.class);
//        System.out.println("反序列化结果: ");
//        System.out.println("map users: " + target.getUserMap().get("users"));
//        System.out.println("map managers: " + target.getUserMap().get("managers"));
//
//        // 可能是个 "BUG" 第一个元素总是为null
//        System.out.println("bean users: " + target.getUsers());
//        System.out.println("bean managers: " + target.getManagers());
//        
//        System.out.println(JSON.toJSONString(target));
        
        Assert.assertNotNull(target.getUsers().get(0));
        Assert.assertNotNull(target.getManagers().get(0));
    }

    private static class Person {
        private String name;
        private Integer age;

        public Person(){}

        public Person(String name, Integer age) {
            super();
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }
    }

    private static class PersonAll {
        private Map<String, List<Person>> userMap = new HashMap<String, List<Person>>();
        private Integer count;
        private List<Person> users;
        private List<Person> managers;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Map<String, List<Person>> getUserMap() {
            return userMap;
        }

        public void setUserMap(Map<String, List<Person>> userMap) {
            this.userMap = userMap;
        }

        public List<Person> getUsers() {
            return users;
        }

        public void setUsers(List<Person> users) {
            this.users = users;
        }

        public List<Person> getManagers() {
            return managers;
        }

        public void setManagers(List<Person> managers) {
            this.managers = managers;
        }
    }
}
