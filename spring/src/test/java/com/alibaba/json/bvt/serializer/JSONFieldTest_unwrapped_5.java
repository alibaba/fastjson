package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

public class JSONFieldTest_unwrapped_5 extends TestCase {

    public void test_jsonField() throws Exception {
        Health vo = new Health();
        vo.id = 123;
        vo.details.put("latitude", 37);
        vo.details.put("longitude", 127);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"id\":123,\"latitude\":37,\"longitude\":127}", text);

        Health vo2 = JSON.parseObject(text, Health.class);
        assertNotNull(vo2.details);
        assertEquals(37, vo2.details.get("latitude"));
        assertEquals(127, vo2.details.get("longitude"));

    }

    public void test_null() throws Exception {
        Health vo = new Health();
        vo.id = 123;
        vo.details = null;

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"id\":123}", text);
    }

    public void test_empty() throws Exception {
        Health vo = new Health();
        vo.id = 123;

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"id\":123}", text);
    }

    public static class Health {
        @JSONField(ordinal = 1)
        public int id;

        @JSONField(unwrapped = true, ordinal = 2)
        public Map<String, Object> details = new LinkedHashMap<String, Object>();
    }
}
