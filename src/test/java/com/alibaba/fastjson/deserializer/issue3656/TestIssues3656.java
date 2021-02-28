package com.alibaba.fastjson.deserializer.issue3656;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-10-10
 *
 * https://github.com/alibaba/fastjson/issues/569
 */
public class TestIssues3656 {


    private String jsonData = "{\n" +
            "    \"serviceType\":\"dubbo\",\n" +
            "    \"types\":[\n" +
            "        {\n" +
            "            \"enums\":[\n" +
            "            ],\n" +
            "            \"typeBuilderName\":\"DefaultTypeBuilder\",\n" +
            "            \"type\":\"int\",\n" +
            "            \"items\":[\n" +
            "            ],\n" +
            "            \"properties\":{\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Before
    public void init() {

    }

    //复现
    @Test
    public void testBug569() {

        MainFastJSON.Metadata m = JSON.parseObject(jsonData, MainFastJSON.Metadata.class);
        System.out.println("types size:" + m.types.size());
        System.out.println(JSON.toJSONString(m));
    }

    //修复
    @Test
    public void testFixBug569() {

    }

}
