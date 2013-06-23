package com.alibaba.json.bvt.parser.deser;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class MapTest extends TestCase {

    public void test_0() throws Exception {
        Map<String, String> map = JSON.parseObject("{id:33}", new TypeReference<Map<String, String>>() {
        });

        Assert.assertEquals(1, map.size());
        Assert.assertEquals("33", map.get("id"));
    }
    
    public void test_1() throws Exception {
        Map<String, BigDecimal> map = JSON.parseObject("{id:33}", new TypeReference<Map<String, BigDecimal>>() {
        });

        Assert.assertEquals(1, map.size());
        Assert.assertEquals(new BigDecimal("33"), map.get("id"));
    }
}
