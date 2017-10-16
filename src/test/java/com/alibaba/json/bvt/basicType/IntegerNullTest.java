package com.alibaba.json.bvt.basicType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.io.StringReader;

/**
 * Created by wenshao on 10/08/2017.
 */
public class IntegerNullTest extends TestCase {
    public void test_null() throws Exception {
        Model model = JSON.parseObject("{\"v1\":null,\"v2\":null}", Model.class);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public void test_null_quote() throws Exception {
        Model model = JSON.parseObject("{\"v1\":\"null\",\"v2\":\"null\"}", Model.class);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public void test_null_1() throws Exception {
        Model model = JSON.parseObject("{\"v1\":null ,\"v2\":null }", Model.class);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public void test_null_1_quote() throws Exception {
        Model model = JSON.parseObject("{\"v1\":\"null\" ,\"v2\":\"null\" }", Model.class);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public void test_null_array() throws Exception {
        Model model = JSON.parseObject("[\"null\" ,\"null\"]", Model.class, Feature.SupportArrayToBean);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public void test_null_array_reader() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[\"null\" ,\"null\"]"), Feature.SupportArrayToBean);
        Model model = reader.readObject(Model.class);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public void test_null_array_reader_1() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[null ,null]"), Feature.SupportArrayToBean);
        Model model = reader.readObject(Model.class);
        assertNotNull(model);
        assertNull(model.v1);
        assertNull(model.v2);
    }

    public static class Model {
        public Integer v1;
        public Integer v2;
    }
}
