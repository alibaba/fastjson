package com.alibaba.json.bvt.ref;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestRef6 extends TestCase {

    public void test_0() throws Exception {
        Family family = new Family();

        Person pA = new Person("a");

        Person pB = new Person("b");

        family.setMembers(new Person[] { pA, pB });
        family.setMaster(pA);

        Family[] familyArray = new Family[] { family };
        String text = JSON.toJSONString(familyArray);
        System.out.println(text);

        Family[] result = JSON.parseObject(text, Family[].class);

        Assert.assertSame(result[0].getMaster(), result[0].getMembers()[0]);

    }

    public static class Family {

        private Person   master;
        private Person[] members;

        public Person getMaster() {
            return master;
        }

        public void setMaster(Person master) {
            this.master = master;
        }

        public Person[] getMembers() {
            return members;
        }

        public void setMembers(Person[] members) {
            this.members = members;
        }

    }

    public static class Person {

        private String name;

        public Person(){

        }

        public Person(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
