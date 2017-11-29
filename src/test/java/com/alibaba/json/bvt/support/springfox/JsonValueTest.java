package com.alibaba.json.bvt.support.springfox;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;
import springfox.documentation.spring.web.json.Json;

public class JsonValueTest extends TestCase {
    public void test_0() throws Exception {
        Json json = new Json("\"{\"id\":1001\"}");
        String text = JSON.toJSONString(json);
        Assert.assertEquals("\"{\"id\":1001\"}", text);
    }
}
