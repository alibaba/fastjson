package com.alibaba.fastjson.serializer.issue3824;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestIssue3824 {


    class X implements Serializable {
        int a;
        public int getA() {
            return a;
        }
        public void setA(int a) {
            this.a = a;
        }
    }


    @Test
    public void testIssue3084() {
        Map<String, Object> result  = new HashMap<>();
        X a = new  X();
        a.a = 100;
        List< X> t = Arrays.asList(a);
        Map<String, Object> y = new HashMap<>();
        y.put("2_wdk", t);
        Map<String, Object> x = new HashMap<>();
        x.put("y", y);
        result.put("a.b.c", x);
        result.put("x", a);
        String s = JSON.toJSONString(result);
        System.out.println(s); // {"a.b.c":{"y":{"2\\_wdk":[{"a":100}]}},"x":{"$ref":"$.a\\.b\\.c.y.2\\\\\\_wdk[0]"}}
        JSONObject revert = JSON.parseObject(s);
        System.out.println(JSON.toJSONString(revert)); // {"a.b.c":{"y":{"2\\_wdk":[{"a":100}]}},"x":[]}
        Assert.assertEquals(JSON.toJSONString(revert),s);

    }
}
