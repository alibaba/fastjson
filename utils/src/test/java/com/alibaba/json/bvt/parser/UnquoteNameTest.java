package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import junit.framework.TestCase;

public class UnquoteNameTest extends TestCase {
    public void test_unquote() throws Exception {
        String text = "{_id:1001}";
        
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertEquals(1001, model._id);
    }
    
    public void test_unquote_parse() throws Exception {
        String text = "{ _id:1001}";
        
        JSONObject model = JSON.parseObject(text);
        Assert.assertEquals(1001, model.get("_id"));
    }

    public void test_unquote_parse_1() throws Exception {
        String text = "{ $id:1001}";

        JSONObject model = JSON.parseObject(text);
        Assert.assertEquals(1001, model.get("$id"));
    }
    
    public void test_unquote_reader() throws Exception {
        String text = "{_id:1001}";
        
        JSONReader reader = new JSONReader(new StringReader(text));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals(1001, model._id);
        reader.close();
    }
    
    public void test_unquote_reader_parse() throws Exception {
        String text = "{_id:1001}";
        
        JSONReader reader = new JSONReader(new StringReader(text));
        JSONObject model = (JSONObject) reader.readObject();
        Assert.assertEquals(1001, model.get("_id"));
        reader.close();
    }
    
    public void test_obj() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{_id:123}"));

        reader.startObject();
        Assert.assertEquals("_id", reader.readString());
        Assert.assertEquals(Integer.valueOf(123), reader.readInteger());
        reader.endObject();

        reader.close();
    }

    public void test_obj_1() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{$id:123}"));

        reader.startObject();
        Assert.assertEquals("$id", reader.readString());
        Assert.assertEquals(Integer.valueOf(123), reader.readInteger());
        reader.endObject();

        reader.close();
    }
    
    public static class Model {
        public int _id;
    }
}
