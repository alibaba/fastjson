package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Bug_for_zhuel extends TestCase {

    public void test_for_zhuel() throws Exception {
        Person[] ps = new Person[3];
        Person p1 = new Person();
        p1.setAge(50);
        p1.setHight("170");
        p1.setId("p1's id");
        p1.setName("person1's name");
        p1.setNames(new String[] { "p1's id", "person1's name" });
        p1.setSex("男");
        Person p2 = new Person();
        p2.setAge(48);
        p2.setHight("155");
        p2.setId("p2's id");
        p2.setName("person2's name");
        p2.setNames(new String[] { "p2's id", "person2's name" });
        p2.setSex("女");
        Person p3 = new Person();
        p3.setAge(10);
        p3.setHight("120");
        p3.setId("p3's id ");
        p3.setName("son's name");
        p3.setNames(new String[] { "p3's id ", "son's name" });
        p3.setSex("男");
        ps[0] = p1;
        ps[1] = p2;
        ps[2] = p3;

        Person[] ps1 = new Person[3];
        Person pp1 = new Person();
        pp1.setAge(52);
        pp1.setHight("170");
        pp1.setId("pp1's id");
        pp1.setName("personpp1's name");
        pp1.setNames(new String[] { "pp1's id", "personpp1's name" });
        pp1.setSex("男");
        Person pp2 = new Person();
        pp2.setAge(49);
        pp2.setHight("150");
        pp2.setId("pp2's id");
        pp2.setName("personpp2's name");
        pp2.setNames(new String[] { "pp2's id", "personpp2's name" });
        pp2.setSex("女");
        Person pp3 = new Person();
        pp3.setAge(10);
        pp3.setHight("125");
        pp3.setId("pp3's id");
        pp3.setName("daughter's name");
        pp3.setNames(new String[] { "pp3's id", "daughter's name" });
        pp3.setSex("女");
        ps1[0] = pp1;
        ps1[1] = pp2;
        ps1[2] = pp3;

        Person[] ps2 = new Person[3];
        Person a1 = new Person();
        a1.setAge(52);
        a1.setHight("170");
        a1.setId("a1's id");
        a1.setName("a1's name");
        a1.setNames(new String[] { "a1's id", "a1's name" });
        a1.setSex("男");
        Person a2 = new Person();
        a2.setAge(49);
        a2.setHight("150");
        a2.setId("a2's id");
        a2.setName("a2's name");
        a2.setNames(new String[] { "a2's id", "a2's name" });
        a2.setSex("女");
        Person a3 = new Person();
        a3.setAge(10);
        a3.setHight("125");
        a3.setId("a3's id");
        a3.setName("daughter's name");
        a3.setNames(new String[] { "a3's id", "daughter's name" });
        a3.setSex("女");
        ps2[0] = a1;
        ps2[1] = a2;
        ps2[2] = a3;

        Family f1 = new Family();
        f1.setId("f1's id");
        f1.setAddress("f1's address");
        f1.setChildrennames(new String[] { "p1's name", "p2's name", "p3's name" });
        f1.setIncome(100000000);
        f1.setMaster(p1);
        f1.setName("person1's home");
        f1.setPs(ps);
        f1.setTest(1994.08);

        Family f2 = new Family();
        f2.setId("f2's id");
        f2.setAddress("f2's address");
        f2.setChildrennames(new String[] { "pp1's name", "pp2's name", "pp3's name" });
        f2.setIncome(100000000);
        f2.setMaster(pp1);
        f2.setName("personpp1's home");
        f2.setPs(ps1);
        Family f3 = new Family();
        f3.setId("f3's id");
        f3.setAddress("f3's address");
        f3.setChildrennames(new String[] { "a1's name", "a2's name", "a3's name" });
        f3.setIncome(100000000);
        f3.setMaster(a1);
        f3.setName("a1's home");
        f3.setPs(ps2);
        f3.setTest(1995.08);
        Family[] fs = new Family[3];
        fs[0] = f1;
        fs[1] = f2;
        fs[2] = f3;
        System.out.println(JSON.VERSION);
        String sfs = JSON.toJSONString(fs, true);

        Assert.assertSame(fs[0].getMaster(), fs[0].getPs()[0]);
        System.out.println(sfs);

        {
            Family[] result = JSON.parseObject(sfs, Family[].class);
            Assert.assertSame(result[0].getMaster(), result[0].getPs()[0]);
            Assert.assertSame(result[1].getMaster(), result[1].getPs()[0]);
            Assert.assertSame(result[2].getMaster(), result[2].getPs()[0]);
        }
        {
            JSONArray array = JSON.parseArray(sfs);
            for (int i = 0; i < array.size(); ++i) {
                JSONObject jsonObj = array.getJSONObject(i);
                Assert.assertSame(jsonObj.get("master"), jsonObj.getJSONArray("ps").get(0));
            }
        }
    }

    public static class Family {

        private String   id;
        private String   name;
        private Person[] ps;
        private String   address;
        private String[] childrennames;
        private Person   master;
        private long     income;
        private double   test;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Person[] getPs() {
            return ps;
        }

        public void setPs(Person[] ps) {
            this.ps = ps;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String[] getChildrennames() {
            return childrennames;
        }

        public void setChildrennames(String[] childrennames) {
            this.childrennames = childrennames;
        }

        public Person getMaster() {
            return master;
        }

        public void setMaster(Person master) {
            this.master = master;
        }

        public long getIncome() {
            return income;
        }

        public void setIncome(long income) {
            this.income = income;
        }

        public double getTest() {
            return test;
        }

        public void setTest(double test) {
            this.test = test;
        }
    }

    public static class Person {

        private String   id;
        private String   name;
        private String   sex;
        private int      age;
        private String[] names;
        private String   hight;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String[] getNames() {
            return names;
        }

        public void setNames(String[] names) {
            this.names = names;
        }

        public String getHight() {
            return hight;
        }

        public void setHight(String hight) {
            this.hight = hight;
        }

    }

}
