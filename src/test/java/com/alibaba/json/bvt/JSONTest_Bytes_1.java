package com.alibaba.json.bvt;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;

public class JSONTest_Bytes_1 extends TestCase {

    public void test_bytes() throws Exception {
        Assert.assertEquals("\"abc\"", new String(JSON.toJSONBytes("abc", SerializeConfig.getGlobalInstance())));
    }
}
