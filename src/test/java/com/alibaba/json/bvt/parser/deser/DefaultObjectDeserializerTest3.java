package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("rawtypes")
public class DefaultObjectDeserializerTest3 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.AbstractSerializeTest2");
    }

    public void test_0() throws Exception {
        HashMap o = (HashMap) JSON.parse("{\"@type\":\"java.lang.Cloneable\"}");
        Assert.assertEquals(0, o.size());
    }
}
