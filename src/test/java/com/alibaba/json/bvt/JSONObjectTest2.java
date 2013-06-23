package com.alibaba.json.bvt;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONObjectTest2 extends TestCase {

    public void test_0() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        JSONObject obj = new JSONObject(map);

        Assert.assertEquals(obj.size(), map.size());

        map.put("a", 1);
        Assert.assertEquals(obj.size(), map.size());
        Assert.assertEquals(obj.get("a"), map.get("a"));

        map.put("b", new int[] { 1 });
        JSONArray array = obj.getJSONArray("b");
        Assert.assertEquals(array.size(), 1);

        map.put("c", new JSONArray());
        JSONArray array2 = obj.getJSONArray("b");
        Assert.assertEquals(array2.size(), 1);

        Assert.assertEquals(obj.getByteValue("d"), 0);
        Assert.assertEquals(obj.getShortValue("d"), 0);
        Assert.assertTrue(obj.getFloatValue("d") == 0F);
        Assert.assertTrue(obj.getDoubleValue("d") == 0D);
        Assert.assertEquals(obj.getBigInteger("d"), null);
        Assert.assertEquals(obj.getSqlDate("d"), null);
        Assert.assertEquals(obj.getTimestamp("d"), null);

        JSONObject obj2 = (JSONObject) obj.clone();
        Assert.assertEquals(obj.size(), obj2.size());
    }
}
