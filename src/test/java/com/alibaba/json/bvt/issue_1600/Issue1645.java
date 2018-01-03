package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.time.LocalDateTime;

public class Issue1645 extends TestCase {
    public void test_for_issue() throws Exception {
        String test = "{\"name\":\"test\",\"testDateTime\":\"2017-12-08 14:55:16\"}";
        JSON.toJSONString(JSON.parseObject(test).toJavaObject(TestDateClass.class), SerializerFeature.PrettyFormat);
    }

    public static class TestDateClass{
        public String name;
        public LocalDateTime testDateTime;
    }
}
