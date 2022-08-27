package com.alibaba.json.bvt.parser.deser;

import java.util.TreeMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TreeMapDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        TreeMap treeMap = JSON.parseObject("{}", TreeMap.class);
        Assert.assertEquals(0, treeMap.size());
    }
}
