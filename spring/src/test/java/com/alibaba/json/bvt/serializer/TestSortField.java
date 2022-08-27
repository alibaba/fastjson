package com.alibaba.json.bvt.serializer;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestSortField extends TestCase {

    public void test_0() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("nnn", "123");

        map.put("13", "123");

        String text = JSON.toJSONString(map);
        
        Assert.assertEquals("{\"nnn\":\"123\",\"13\":\"123\"}", text);

    }
}
