package com.alibaba.json.bvt.parser;

import java.net.InetSocketAddress;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class InetSocketAddressTest extends TestCase {

    public void test_parse() throws Exception {
        JSON.parseObject("{\"address\":'10.20.133.23',\"port\":123,\"xx\":33}", InetSocketAddress.class);
    }

    public void test_parse_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"address\":'10.20.133.23',\"port\":'12.3',\"xx\":33}", InetSocketAddress.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
