package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class Bug_for_dongqi extends TestCase {
    public void test_bug() throws Exception {
        Map<String, Object> obj = new HashMap<String,Object>();
        obj.put("value", "；\r\n3、 公");
        System.out.print(JSON.toJSONString(obj));
        Assert.assertEquals("{\"value\":\"；\\r\\n3、\\u009E 公\"}", JSON.toJSONString(obj));
    }
}
