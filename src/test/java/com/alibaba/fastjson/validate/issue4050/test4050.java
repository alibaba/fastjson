package com.alibaba.fastjson.validate.issue4050;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import org.junit.Test;
import junit.framework.TestCase;

import java.util.Map;

public class test4050 extends TestCase{

    @Test
    public void test1() throws Exception{
        String json="{\"file\":\"d:\\ttt.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }

    @Test
    public void test2() throws Exception{
        String json="{\"file\":\"d:\\nnn.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }

    @Test
    public void test3() throws Exception{
        String json="{\"file\":\"d:\\rrr.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }

    @Test
    public void test4() throws Exception{
        String json="{\"file\":\"d:\\abc.txt\"}";
//        Map<String, String> map = JSON.parseObject(json, Map.class);
//        System.out.println(map);
        assertFalse(JSONValidator.from(json).validate());
    }

    @Test
    public void test5() throws Exception{
        String json="{\"file\":\"d:\\bbb.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }

    @Test
    public void test6() throws Exception{
        String json="{\"file\":\"d:\\ccc.txt\"}";
//        Map<String, String> map = JSON.parseObject(json, Map.class);
//        System.out.println(map);
        assertFalse(JSONValidator.from(json).validate());
    }

    @Test
    public void test7() throws Exception{
        String json="{\"file\":\"d:\\ddd.txt\"}";
//        Map<String, String> map = JSON.parseObject(json, Map.class);
//        System.out.println(map);
        assertFalse(JSONValidator.from(json).validate());
    }

    @Test
    public void test8() throws Exception{
        String json="{\"file\":\"d:\\eee.txt\"}";
//        Map<String, String> map = JSON.parseObject(json, Map.class);
//        System.out.println(map);
        assertFalse(JSONValidator.from(json).validate());
    }

    @Test
    public void test9() throws Exception{
        String json="{\"file\":\"d:\\fff.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }

    @Test
    public void test10() throws Exception{
        String json="{\"file\":\"d:\\vvv.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }


    @Test
    public void test11() throws Exception{
        String json="{\"file\":\"d:\\'.txt\"}";
        Map<String, String> map = JSON.parseObject(json, Map.class);
        System.out.println(map);
        assertTrue(JSONValidator.from(json).validate());
    }


}
