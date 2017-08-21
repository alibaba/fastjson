package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class JSONFieldTest_unwrapped_1 extends TestCase {

    public void test_jsonField() throws Exception {
        VO vo = new VO();
        vo.id = 123;
        vo.properties.put("latitude", 37);
        vo.properties.put("longitude", 127);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"id\":123,\"latitude\":37,\"longitude\":127}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        assertNotNull(vo2.properties);
        assertEquals(37, vo2.properties.get("latitude"));
        assertEquals(127, vo2.properties.get("longitude"));

    }

    public static class VO {
        public int id;

        @JSONField(unwrapped = true)
        public Map<String, Object> properties = new LinkedHashMap<String, Object>();
    }
}
