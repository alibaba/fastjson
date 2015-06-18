package com.alibaba.json.bvt;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class IntKeyMapTest extends TestCase {

    public void test_0() throws Exception {
        JSON.parse("{1:\"AA\",2:{}}");
    }
}
