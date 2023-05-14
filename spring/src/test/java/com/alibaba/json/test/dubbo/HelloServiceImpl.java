/**
 * Project: dubbo.hello.sample.service
 *
 * File Created at 2009-6-12
 * $Id$
 *
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.json.test.dubbo;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


/**
 * TODO Comment of HelloServiceImpl
 * 
 * @author tony.chenl
 */
public class HelloServiceImpl {

    public Tigers eatTiger(Tiger tiger) {

        return new Tigers(tiger);
    }

    public String eatTiger() {
        return "想吃老虎";
    }

    public String eatTiger(String number) {
        return number;
    }

    public HashSet<String> eatTigers(String name, HashSet<String> tigers) {
        return tigers;
    }

    public Map<String, Collection<String>> eatTiger(String name, HashSet<String> tigers) {
        HashMap<String, Collection<String>> tiger = new HashMap<String, Collection<String>>();
        tiger.put(name, tigers);
        return tiger;
    }

    public String eatTiger(String name, Tiger tiger) {
        return name + "想吃" + tiger.getTigerName() + tiger.getTigerSex();
    }

    public EnumTest eatEnums(EnumTest... enums) {
        if (enums.length > 0) {
            return enums[enums.length - 1];
        } else {
            return EnumTest.Cat;
        }
    }

    public Date eatTime(Date date) {
        return date;
    }

    @SuppressWarnings("rawtypes")
    public Map eatTiger(Map map) {
        return map;
    }

    public Map<String, String> eatTigerMap(Map<String, String> map) {
        return map;
    }

    public LinkedHashMap<String, String> eatTiger(LinkedHashMap<String, String> map) {
        return map;
    }

    public ConcurrentHashMap<String, String> eatTiger(ConcurrentHashMap<String, String> map) {
        return map;
    }

    public String sayHello(String hello) {
        for (int i = 0; i < 10000; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(new Random().nextInt(5000));
        }
        return hello;
    }

    public Person showPerson(Person person) {
        return person;
    }

    public List<Person> eatTiger(List<Person> list) {
        return list;
    }

    public String[] eatTiger(String[] args) {
        return args;
    }
}
