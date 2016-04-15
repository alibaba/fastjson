package com.alibaba.json.bvt.parser.scanNumberValue;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class ScanNumberValueTest1 extends TestCase {
    public void test_0() throws Exception {
        String text = "{\"v1\":10001,\"v2\":-20001,\"v3\":1B,\"v4\":1S,\"v5\":1L,\"v6\":1F,\"v7\":1D}";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(10001, obj.get("v1"));
        Assert.assertEquals(-20001, obj.get("v2"));
        Assert.assertEquals(Byte.valueOf((byte) 1), (Byte) obj.get("v3"));
        Assert.assertEquals(Short.valueOf((short) 1), (Short) obj.get("v4"));
        Assert.assertEquals(Long.valueOf((int) 1), (Long) obj.get("v5"));
        Assert.assertEquals(Float.valueOf((int) 1), (Float) obj.get("v6"));
        Assert.assertEquals(Double.valueOf((int) 1), (Double) obj.get("v7"));
    }
}
