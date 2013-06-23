package com.alibaba.json.bvt.dubbo;

import java.util.ArrayList;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.dubbo.FullAddress;
import com.alibaba.json.test.dubbo.HelloServiceImpl;
import com.alibaba.json.test.dubbo.Person;
import com.alibaba.json.test.dubbo.PersonInfo;
import com.alibaba.json.test.dubbo.PersonStatus;
import com.alibaba.json.test.dubbo.Phone;
import com.alibaba.json.test.dubbo.Tiger;
import com.alibaba.json.test.dubbo.Tigers;

public class TestForDubbo extends TestCase {

    static Person            person;

    static {
        person = new Person();
        person.setPersonId("superman111");
        person.setLoginName("superman");
        person.setEmail("sm@1.com");
        person.setPenName("pname");
        person.setStatus(PersonStatus.ENABLED);

        ArrayList<Phone> phones = new ArrayList<Phone>();
        Phone phone1 = new Phone("86", "0571", "87654321", "001");
        Phone phone2 = new Phone("86", "0571", "87654322", "002");
        phones.add(phone1);
        phones.add(phone2);
        PersonInfo pi = new PersonInfo();
        pi.setPhones(phones);
        Phone fax = new Phone("86", "0571", "87654321", null);
        pi.setFax(fax);
        FullAddress addr = new FullAddress("CN", "zj", "3480", "wensanlu", "315000");
        pi.setFullAddress(addr);
        pi.setMobileNo("13584652131");
        pi.setMale(true);
        pi.setDepartment("b2b");
        pi.setHomepageUrl("www.capcom.com");
        pi.setJobTitle("qa");
        pi.setName("superman");
        person.setInfoProfile(pi);
    }

    private HelloServiceImpl helloService = new HelloServiceImpl();

    public void f_testParamType4() {
        Tiger tiger = new Tiger();
        tiger.setTigerName("东北虎");
        tiger.setTigerSex(true);
        Tigers tigers = helloService.eatTiger(tiger);

        String text = JSON.toJSONString(tigers, SerializerFeature.WriteClassName);
        System.out.println(text);

        Tigers tigers2 = JSON.parseObject(text, Tigers.class);

        Assert.assertEquals(text, JSON.toJSONString(tigers2, SerializerFeature.WriteClassName));
    }

    public void testPerson() {
        Person p = helloService.showPerson(person);
        String text = JSON.toJSONString(p, SerializerFeature.WriteClassName);
        System.out.println(text);
        
        Person result = JSON.parseObject(text, Person.class);
        
        assertEquals(result.getInfoProfile().getPhones().get(0).getArea(),
                     person.getInfoProfile().getPhones().get(0).getArea());
        assertEquals(result.getInfoProfile().getPhones().get(0).getCountry(),
                     person.getInfoProfile().getPhones().get(0).getCountry());
        assertEquals(result.getInfoProfile().getPhones().get(0).getExtensionNumber(),
                     person.getInfoProfile().getPhones().get(0).getExtensionNumber());
        assertEquals(result.getInfoProfile().getPhones().get(0).getNumber(),
                     person.getInfoProfile().getPhones().get(0).getNumber());
    }
}
