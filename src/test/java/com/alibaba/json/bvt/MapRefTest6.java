package com.alibaba.json.bvt;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class MapRefTest6 extends TestCase {

    public void test_0() throws Exception {
        List<Map<Object, Object>> list = JSON.parseObject("[{},{\"$\":\"$[0]\"},{\"$001\":\"101\"},{\"$r01\":\"102\"},{\"$re1\":\"103\"}]",
                                                          new TypeReference<List<Map<Object, Object>>>() {
                                                          });
        Assert.assertEquals(5, list.size());
        Assert.assertEquals(true, ((Map)list.get(0)).isEmpty());
        Assert.assertEquals("$[0]", ((Map)list.get(1)).get("$"));
        Assert.assertEquals("101", ((Map)list.get(2)).get("$001"));
        Assert.assertEquals("102", ((Map)list.get(3)).get("$r01"));
        Assert.assertEquals("103", ((Map)list.get(4)).get("$re1"));
    }


}
