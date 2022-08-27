package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JSONPath_15
        extends TestCase {
    final static String a = "{code:1,msg:'Hello world',data:{list:[1,2,3,4,5], ary2:[{a:2},{a:3,b:{c:'ddd'}}]}}";
    final static String b = "[{b:{c:1}}, {b:{d:1}}, {b:{c:2}}, {b:{c:23}}]";
    final static String c = "[{c:'aaaa'}, {b:'cccc'}, {c:'cccaa'}]";

    public void test_0() {

        JSONObject object = JSON.parseObject(a);

        List<Object> items = (List<Object>) JSONPath.eval(object, "data.ary2[*].b.c");
        assertEquals("[\"ddd\"]", JSON.toJSONString(items));
    }

    public void test_1() {
        Object object = JSON.parse(b);

        List<Object> items = (List<Object>) JSONPath.eval(object, "$..b[?(@.c == 23)]");
        assertEquals("[{\"c\":23}]", JSON.toJSONString(items));
    }

    public void test_2() {
        Object object = JSON.parse(b);

        Object min = JSONPath.eval(object, "$..c.min()");
        assertEquals("1", JSON.toJSONString(min));
    }

    public void test_3() {
        Object object = JSON.parse(c);

        Object min = JSONPath.eval(object, "$[?(@.c =~ /a+/)]");
        assertEquals("[{\"c\":\"aaaa\"}]", JSON.toJSONString(min));
    }
//
//    public void test_c() {
//        Object object = JSON.parse(c);
//
//        Object min = JSONPath.eval(object, "data.list[?(@ in $..ary2[0].a)]");
//        assertEquals("[{\"c\":\"aaaa\"}]", JSON.toJSONString(min));
//    }
}
