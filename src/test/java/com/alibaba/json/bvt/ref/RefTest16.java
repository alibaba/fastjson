package com.alibaba.json.bvt.ref;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class RefTest16 extends TestCase {

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
        String text = JSON.toJSONString(familyArray);
        System.out.println(text);

        Family[] result = JSON.parseObject(text, Family[].class);

        Assert.assertSame(result[0].getMaster(), result[0].getMembers()[0]);
        Assert.assertSame(result[1].getMaster(), result[1].getMembers()[0]);

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
