package com.alibaba.json.bvt.parser;

import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class JSONScannerTest_scanFieldStringArray extends TestCase {

    public void test_string() throws Exception {
        String text = "{\"value\":[1]}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals("[1]", obj.getValue().toString());
    }

    public void test_string_1() throws Exception {
        String text = "{\"value\":[\"1\"]}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals("[1]", obj.getValue().toString());
    }
    
    public void test_string_2() throws Exception {
        String text = "{\"value\":['1']}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals("[1]", obj.getValue().toString());
    }
    
    public void test_string_3() throws Exception {
        String text = "{\"value\":[\"1\\t2\"]}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals("[1\t2]", obj.getValue().toString());
    }
    
    public void test_string_4() throws Exception {
        String text = "[{\"value\":[\"1\"]}]";
        List<VO> list = JSON.parseArray(text, VO.class);
        Assert.assertEquals("[1]", list.get(0).getValue().toString());
    }
    
    public void test_string_5() throws Exception {
        String text = "[{\"value\":[\"1\"]},{\"value\":[\"2\"]}]";
        List<VO> list = JSON.parseArray(text, VO.class);
        Assert.assertEquals("[1]", list.get(0).getValue().toString());
        Assert.assertEquals("[2]", list.get(1).getValue().toString());
    }
    
    public void test_string_error() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":{}}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException e) {
            error = e;
        }
        //Assert.assertNotNull(error);
    }

    public void test_string_error_2() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":[\"1\"}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_string_error_3() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":[\"1\"]}}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_string_error_4() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":[\"1\"]]";
            JSON.parseObject(text, VO.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_string_error_5() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":[\"1\"]}{";
            JSON.parseObject(text, VO.class);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private List<String> value;

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

    }
}
