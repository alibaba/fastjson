package com.alibaba.json.bvt.parser;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JSONArrayParseTest extends TestCase {
    public void test_array() throws Exception {
        String text = "[{id:123}]";
        List<Map<String, Integer>> array = JSON.parseObject(text, new TypeReference<List<Map<String, Integer>>>() {});
        Assert.assertEquals(1, array.size());
        Map<String, Integer> map  = array.get(0);
        Assert.assertEquals(123, map.get("id").intValue());
    }
}
