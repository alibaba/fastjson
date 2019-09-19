package com.alibaba.json.bvt.issue_2400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2464 extends TestCase {
    public void test1() throws Exception {
        String json = "[\"Mjg4NDd8MXxjb20uY2Fpbmlhby5pc2ltdS5xLndvcmtmbG93LmNvbGxlY3Quc2NoZWR1bGUuaW1wbC5UYXNrU3RvcENvbGxlY3RDYWxsQmFja0hhbmRsZXJJbXBsfDB8\",1]";
        Object result =  JSON.parseArray(json,new Class[]{byte[].class,Integer.class});
        assertEquals(json, JSON.toJSONString(result));

        result = JSON.parseArray(json,new Class[]{char[].class,Integer.class});
        assertEquals(json, JSON.toJSONString(result));
    }

    public void test2() throws Exception {
        String json = "[1,\"Mjg4NDd8MXxjb20uY2Fpbmlhby5pc2ltdS5xLndvcmtmbG93LmNvbGxlY3Quc2NoZWR1bGUuaW1wbC5UYXNrU3RvcENvbGxlY3RDYWxsQmFja0hhbmRsZXJJbXBsfDB8\"]";
        Object result =  JSON.parseArray(json,new Class[]{Integer.class,byte[].class});
        assertEquals(json, JSON.toJSONString(result));

        result = JSON.parseArray(json,new Class[]{Integer.class, char[].class});
        assertEquals(json, JSON.toJSONString(result));
    }

    public void test3() throws Exception {
        String json = "[1,\"aaa\",\"bbb\",\"ccc\"]";
        Object result = JSON.parseArray(json, new Class[]{Integer.class, String[].class});
        assertEquals("[1,[\"aaa\",\"bbb\",\"ccc\"]]", JSON.toJSONString(result));
    }

    public void test4() throws Exception {
        String json = "[1,97,98,99]";
        Object result = JSON.parseArray(json, new Class[]{Integer.class, byte[].class});
        assertEquals("[1,\"YWJj\"]", JSON.toJSONString(result));
    }
}
