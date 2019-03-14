package com.alibaba.json.bvt.issue_2000;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Objects;

/**
 * @author machunxiao create at 2019-03-14
 */
public class Issue2019 extends TestCase {

    public void test_for_issue() throws Exception {
        People people = new People();
        people.name = "tom";
        people.address = "sh";
        people.age = 20;
        String str = JSON.toJSONString(people);
        Assert.assertEquals("{\"address\":\"sh\",\"age\":20,\"name\":\"tom\"}", str);
        People p1 = JSON.parseObject(str, People.class);
        Assert.assertEquals(people, p1);

    }

    public static class People {

        private String name;
        private String address;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getUsername() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            People people = (People) o;
            return age == people.age &&
                    Objects.equals(name, people.name) &&
                    Objects.equals(address, people.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, address, age);
        }
    }
}
