package com.alibaba.json.bvt.ref;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class RefTest17 extends TestCase {

    public void test_0() throws Exception {

        Person pA = new Person("a");
        Person pB = new Person("b");

        Family fA = new Family();
        fA.setMembers(new Person[] { pA, pB });
        fA.setMaster(pA);

        Person pC = new Person("c");
        Person pD = new Person("d");

        Family fB = new Family();
        fB.setMembers(new Person[] { pC, pD });
        fB.setMaster(pC);

        Family[] familyArray = new Family[] { fA, fB };
        String text = JSON.toJSONString(familyArray, true);
        System.out.println(text);

        JSONArray array = JSON.parseArray(text);

        Assert.assertSame(array.getJSONObject(0).get("master"), array.getJSONObject(0).getJSONArray("members").get(0));
        
        Family family = array.getObject(0, Family.class);
        Assert.assertNotNull(family.getMembers()[0]);
        Assert.assertNotNull(family.getMembers()[1]);
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
