package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class JSONFieldTest_unwrapped_6 extends TestCase {

    public void test_jsonField() throws Exception {
        Health vo = new Health();
        List<String> cities = new ArrayList<String>();
        cities.add("Beijing");
        cities.add("Shanghai");
        vo.id = 123;
        vo.cities = cities;

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"cities\":[\"Beijing\",\"Shanghai\"],\"id\":123}", text);

        Health vo2 = JSON.parseObject(text, Health.class);
        assertNotNull(vo2.cities);
        assertEquals("Beijing", vo2.cities.get(0));
        assertEquals("Shanghai", vo2.cities.get(1));

    }

    public void test_null() throws Exception {
        Health vo = new Health();
        vo.id = 123;
        vo.cities = null;

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
        @JSONField(unwrapped = true)
        public int id;

        @JSONField(unwrapped = true)
        public List<String> cities;
    }
}
