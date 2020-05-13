package com.alibaba.json.bvt;

import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONTest_Bytes extends TestCase {

    @SuppressWarnings("rawtypes")
    public void test_bytes() throws Exception {
        for (int i = 0; i < 10; ++i) {
            String charset = "UTF-8";
            String text = "{name:'张三', age:27}";

            Map map = JSON.parseObject(text.getBytes(charset), Map.class);
            Assert.assertEquals("张三", map.get("name"));
            Assert.assertEquals(27, map.get("age"));
        }

        for (int i = 0; i < 10; ++i) {
            String charset = "UTF-8";
            String text = "{name:'张三', age:27}";

            JSONObject map = (JSONObject) JSON.parse(text.getBytes(charset));
            Assert.assertEquals("张三", map.get("name"));
            Assert.assertEquals(27, map.get("age"));
        }
    }
}
