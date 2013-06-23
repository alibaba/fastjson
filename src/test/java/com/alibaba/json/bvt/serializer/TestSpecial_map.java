package com.alibaba.json.bvt.serializer;

import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class TestSpecial_map extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("{\"name\":\"\\0\"}", JSON.toJSONString(Collections.singletonMap("name", "\0")));
    }

    public void test_1() throws Exception {
        Assert.assertEquals("{\"name\":\"\\1\"}", JSON.toJSONString(Collections.singletonMap("name", "\1")));
    }

    public void test_2() throws Exception {
        Assert.assertEquals("{\"name\":\"\\2\"}", JSON.toJSONString(Collections.singletonMap("name", "\2")));
    }

    public void test_3() throws Exception {
        Assert.assertEquals("{\"name\":\"\\3\"}", JSON.toJSONString(Collections.singletonMap("name", "\3")));
    }

    public void test_4() throws Exception {
        Assert.assertEquals("{\"name\":\"\\4\"}", JSON.toJSONString(Collections.singletonMap("name", "\4")));
    }
    
    public void test_5() throws Exception {
        Assert.assertEquals("{\"name\":\"\\5\"}", JSON.toJSONString(Collections.singletonMap("name", "\5")));
    }
    
    public void test_6() throws Exception {
        Assert.assertEquals("{\"name\":\"\\6\"}", JSON.toJSONString(Collections.singletonMap("name", "\6")));
    }
    
    public void test_7() throws Exception {
        Assert.assertEquals("{\"name\":\"\\7\"}", JSON.toJSONString(Collections.singletonMap("name", "\7")));
    }
    
    public void test_8() throws Exception {
        Assert.assertEquals("{\"name\":\"\\b\"}", JSON.toJSONString(Collections.singletonMap("name", "\b")));
    }
    
    public void test_9() throws Exception {
        Assert.assertEquals("{\"name\":\"\\t\"}", JSON.toJSONString(Collections.singletonMap("name", "\t")));
    }
    
    public void test_10() throws Exception {
        Assert.assertEquals("{\"name\":\"\\n\"}", JSON.toJSONString(Collections.singletonMap("name", "\n")));
    }
    
    
}
