package com.alibaba.fastjson.deserializer.issue4129;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TestIssues4129 {
    @Test
    public void test0() {
        Policy policy = new Policy();
        policy.setTime(new Date(1640966400000L));
        assertEquals(JSONObject.toJSONString(policy),"{\"time\":\"20220101000000\"}");
    }

    @Test
    public void test1(){
        Policy policy = new Policy();
        policy.setTime(new Date(1640966400000L));
        String string = JSONObject.toJSONString(policy);
        policy = JSONObject.parseObject(string).toJavaObject(Policy.class);
        string = JSONObject.toJSONString(policy);
        assertEquals(string,"{\"time\":\"20220101000000\"}");
    }

    @Data
    static  class Policy{
        @JSONField(format = "yyyyMMddHHmmss")
        private Date time;
    }
}
