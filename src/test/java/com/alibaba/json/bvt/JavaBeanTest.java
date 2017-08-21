/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.bvt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class JavaBeanTest extends TestCase {

    public void f_test_toJSON() throws Exception {
        User user = new User();
        user.setName("校长");
        user.setAge(3);
        user.setSalary(new BigDecimal("123456789.0123"));

        String jsonString = JSON.toJSONString(user);

        System.out.println(jsonString);

        JSON.parseObject(jsonString);
        User user1 = JSON.parseObject(jsonString, User.class);

        Assert.assertEquals(user.getAge(), user1.getAge());
        Assert.assertEquals(user.getName(), user1.getName());
        Assert.assertEquals(user.getSalary(), user1.getSalary());
    }

    public void test_toJSON_List() throws Exception {
        User user = new User();
        user.setName("校长");
        user.setAge(3);
        user.setSalary(new BigDecimal("123456789.0123"));
        user.setBirthdate(new Date());
        user.setOld(true);

        List<User> userList = new ArrayList<User>();
        userList.add(user);

        String jsonString = JSON.toJSONString(userList);

        System.out.println(jsonString);

        List<User> userList1 = JSON.parseArray(jsonString, User.class);

        User user1 = userList1.get(0);

        Assert.assertEquals(user.getAge(), user1.getAge());
        Assert.assertEquals(user.getName(), user1.getName());
        Assert.assertEquals(user.getSalary(), user1.getSalary());
        Assert.assertEquals(user.getBirthdate(), user1.getBirthdate());
        Assert.assertEquals(user.isOld(), user1.isOld());
    }

    @SuppressWarnings("unchecked")
    public void f_testComposite() throws Exception {
        Group group = new Group();
        group.setName("神棍");

        User user = new User();
        user.setName("校长");
        user.setAge(3);
        user.setSalary(new BigDecimal("123456789.0123"));

        group.getUsers().add(user);
        ((List<User>) group.getUsers2()).add(user);

        String jsonString = JSON.toJSONString(group);

        System.out.println(jsonString);

        JSON.parseObject(jsonString);
        Group group1 = JSON.parseObject(jsonString, Group.class);
        Assert.assertEquals(group.getName(), group1.getName());

        User user1 = group1.getUsers().get(0);
        Assert.assertEquals(user.getAge(), user1.getAge());
        Assert.assertEquals(user.getName(), user1.getName());
        Assert.assertEquals(user.getSalary(), user1.getSalary());
    }

    public static class User {

        private String     name;
        private int        age;
        private BigDecimal salary;
        private Date       birthdate;
        private boolean    old;

        public boolean isOld() {
            return old;
        }

        public void setOld(boolean old) {
            this.old = old;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

    }

    public static class Group {

        private List<User>           users  = new ArrayList<User>();
        private List<? extends User> users2 = new ArrayList<User>();

        private String               name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public List<? extends User> getUsers2() {
            return users2;
        }

        public void setUsers2(List<? extends User> users2) {
            this.users2 = users2;
        }

    }
}
