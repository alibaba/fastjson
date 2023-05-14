package com.alibaba.json.bvt;

import java.util.Arrays;

import org.junit.Assert;

import com.alibaba.fastjson.JSONArray;

import junit.framework.TestCase;

public class JSONArrayTest3 extends TestCase {
    public void test_0() throws Exception {
        JSONArray array = new JSONArray();
        array.set(1, "1001");
        Assert.assertEquals(2, array.size());
        Assert.assertNull(array.get(0));
        Assert.assertEquals("1001", array.get(1));
        
        array.clear();
        Assert.assertEquals(0, array.size());
        
        array.set(-1, "1001");
        Assert.assertEquals(1, array.size());
        Assert.assertEquals("1001", array.get(0));
        
        array.fluentAdd("1002").fluentClear();
        Assert.assertEquals(0, array.size());
        
        array.fluentAdd("1002").fluentRemove("1002");
        Assert.assertEquals(0, array.size());
        
        array.fluentAdd("1002").fluentRemove(0);
        Assert.assertEquals(0, array.size());
        
        array.fluentSet(1, "1001");
        Assert.assertEquals(2, array.size());
        Assert.assertNull(array.get(0));
        Assert.assertEquals("1001", array.get(1));

        array.fluentRemoveAll(Arrays.asList(null, "1001"));
        Assert.assertEquals(0, array.size());
        
        array.fluentAddAll(Arrays.asList("1001", "1002", "1003"));
        Assert.assertEquals(3, array.size());
        
        array.retainAll(Arrays.asList("1002", "1004"));
        Assert.assertEquals(1, array.size());
        Assert.assertEquals("1002", array.get(0));
    }
}
