package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.time.LocalDateTime;

public class Issue1987 extends TestCase {
    public void test_for_issue() throws Exception {
        JsonExample example = new JsonExample();

        //test1 正确执行, test2, test3 执行出错 com.alibaba.fastjson.JSONException: can not cast to : java.time.LocalDateTime
        example.setTestLocalDateTime(LocalDateTime.now());

        //纳秒数设置为0 ,test1,test2,test3 全部正确执行
        //example.setTestLocalDateTime(LocalDateTime.now().withNano(0));
        String text = JSON.toJSONString(example, SerializerFeature.PrettyFormat);
        System.out.println(text);

        //test1, 全部可以正常执行
        JsonExample example1 = JSON.parseObject(text, JsonExample.class);
        System.out.println(JSON.toJSONString(example1));

        //test2  纳秒数为0, 可以正常执行, 不为0则报错
        JsonExample example2 = JSONObject.parseObject(text).toJavaObject(JsonExample.class);
        System.out.println(JSON.toJSONString(example2));

        //test3 纳秒数为0, 可以正常执行, 不为0则报错
        JsonExample example3 = JSON.parseObject(text).toJavaObject(JsonExample.class);
        System.out.println(JSON.toJSONString(example3));
    }

    public static class JsonExample {

        private LocalDateTime testLocalDateTime;

        public LocalDateTime getTestLocalDateTime() {
            return testLocalDateTime;
        }

        public void setTestLocalDateTime(LocalDateTime testLocalDateTime) {
            this.testLocalDateTime = testLocalDateTime;
        }
    }
}
