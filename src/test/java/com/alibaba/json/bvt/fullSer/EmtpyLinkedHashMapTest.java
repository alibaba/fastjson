package com.alibaba.json.bvt.fullSer;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class EmtpyLinkedHashMapTest extends TestCase {
    public void test_0() throws Exception {
        Map map = (Map) JSON.parseObject("{\"@type\":\"java.util.LinkedHashMap\"}", Object.class);
    }
}
