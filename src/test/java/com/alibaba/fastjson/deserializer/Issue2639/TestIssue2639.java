package com.alibaba.fastjson.deserializer.Issue2639;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

/**
 * @Author:JacceYang  chaoyang_sjtu@126.com
 * @Description:
 * @Data:Initialized in 10:30 AM 2019/8/17
 **/
public class TestIssue2639 {

    @Test
    public  void test_InvalidStartToken(){

        String str=" }";

        Person person = JSONArray.parseObject(str, Person.class);

        System.out.println(person);

    }
}
