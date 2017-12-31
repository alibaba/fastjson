package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.Date;

public class Issue1679 extends TestCase {

    public void test_for_issue() throws Exception {
        String json = "{\"create\":\"2018-01-10 08:30:00\"}";
        User user = JSON.parseObject(json, User.class);
        assertEquals("\"2018-01-10T08:30:00+08:00\"", JSON.toJSONString(user.create, SerializerFeature.UseISO8601DateFormat));
    }

    public static class User{
        public Date create;
    }
}
