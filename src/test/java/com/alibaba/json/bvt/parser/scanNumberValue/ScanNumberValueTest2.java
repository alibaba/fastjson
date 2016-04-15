package com.alibaba.json.bvt.parser.scanNumberValue;

import java.math.BigDecimal;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class ScanNumberValueTest2 extends TestCase {
    public void test_0() throws Exception {
        String text = "{\"v1\":100.01}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(new BigDecimal("100.01"), obj.get("v1"));
    }
    
    public void test_1() throws Exception {
        String text = "{\"v1\":100.01}";
        JSONObject obj = JSON.parseObject(text, JSONObject.class, 0);
        Assert.assertEquals(Double.parseDouble("100.01"), obj.get("v1"));
    }
}
