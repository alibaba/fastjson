package com.alibaba.json.bvt.parser.scanNumberValue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class ScanNumberValueTest3 extends TestCase {
    public void test_0() throws Exception {
        String text = "{\"v1\":92233720368547758070}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(new BigInteger("92233720368547758070"), obj.get("v1"));
    }
    
    public void test_1() throws Exception {
        String text = "{\"v1\":-92233720368547758070}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(new BigInteger("-92233720368547758070"), obj.get("v1"));
    }
    
    public void test_2() throws Exception {
        String text = "{\"v1\":92233720368547758070.334}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(new BigDecimal("92233720368547758070.334"), obj.get("v1"));
    }
    
    public void test_3() throws Exception {
        String text = "{\"v1\":-92233720368547758070.334}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(new BigDecimal("-92233720368547758070.334"), obj.get("v1"));
    }
}
