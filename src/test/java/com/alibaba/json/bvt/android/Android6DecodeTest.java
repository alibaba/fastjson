package com.alibaba.json.bvt.android;

import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class Android6DecodeTest extends TestCase {
    protected void setUp() throws Exception {
        Field field = JSONLexer.class.getDeclaredField("V6");
        field.setAccessible(true);
        field.setBoolean(null, true);;
    }
    
    protected void tearDown() throws Exception {
        Field field = JSONLexer.class.getDeclaredField("V6");
        field.setAccessible(true);
        field.setBoolean(null, false);;
    }
    
    public void test_decode() throws Exception {
        String text = "{\"id\":1001,\"name\":\"wenshao\"}";
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(1001, model.id);
        Assert.assertEquals("wenshao", model.name);
    }
    
    public void test_parse() throws Exception {
        String text = "{\"id\":1001,\"name\":\"wenshao\",\"items_v2_10022352\":{\"id\":5232362}}";
        JSONObject model = JSON.parseObject(text);
        Assert.assertEquals(1001, model.get("id"));
        Assert.assertEquals("wenshao", model.get("name"));
        Assert.assertEquals(5232362, model.getJSONObject("items_v2_10022352").get("id"));
    }
    
    public static class Model {
        public int id;
        public String name;
    }
}
