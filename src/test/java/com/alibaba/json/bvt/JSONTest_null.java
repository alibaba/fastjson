package com.alibaba.json.bvt;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class JSONTest_null extends TestCase {

    public void test_0() throws Exception {
        Assert.assertNull(JSON.parseArray(null));
        Assert.assertNull(JSON.parseArray(""));
        Assert.assertNull(JSON.parseArray("null"));
        Assert.assertNull(JSON.parseArray(null, new Type[] { Object.class, Object.class }));
        Assert.assertNull(JSON.parseObject((char[]) null, 0, int.class, Feature.AllowArbitraryCommas));
        Assert.assertNull(JSON.parseObject(new char[0], 0, int.class, Feature.AllowArbitraryCommas));

        Assert.assertNull(JSON.parseObject("null".toCharArray(), 4, Object.class,  Feature.AllowArbitraryCommas));
        Assert.assertNull(JSON.parseObject("null".toCharArray(), 4, Object.class));
        Assert.assertNull(JSON.parse("null".getBytes(), 0, 4, Charset.forName("UTF-8").newDecoder(), Feature.AllowArbitraryCommas));
        Assert.assertNull(JSON.parse((byte[]) null, 0, 0, Charset.forName("UTF-8").newDecoder(), Feature.AllowArbitraryCommas));
        Assert.assertNull(JSON.parse(new byte[0], 0, 0, Charset.forName("UTF-8").newDecoder(), Feature.AllowArbitraryCommas));
    }
}
