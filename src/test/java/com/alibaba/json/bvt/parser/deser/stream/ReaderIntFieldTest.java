package com.alibaba.json.bvt.parser.deser.stream;

import java.io.StringReader;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class ReaderIntFieldTest extends TestCase {

    public void test_int_error_0() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":1.A}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_int_error_1() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":2147483648}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_int_error_1_x() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":9223372036854775808}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_int_error_1_x1() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":-2147483649}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_int_error_2() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":AA}"));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_int_normal() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"value\":1001,\"value2\":-2001}"));
        Model model = reader.readObject(Model.class);
        Assert.assertEquals(1001, model.value);
        Assert.assertEquals(-2001, model.value2);
        reader.close();
    }

    public void test_int_normal_2() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"value\":3001,\"value2\":-4001}}"));
        Map<String, Model> map = reader.readObject(new TypeReference<Map<String, Model>>() {
        });
        Model model = map.get("model");
        Assert.assertEquals(3001, model.value);
        Assert.assertEquals(-4001, model.value2);
        reader.close();
    }

    public void test_int_error_map() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"model\":{\"value\":3001,\"value2\":-4001}["));
            reader.readObject(new TypeReference<Map<String, Model>>() {
            });
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_int_error_end() throws Exception {
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader("{\"value\":1001,\"value2\":-2001["));
            reader.readObject(Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Model {

        public int value;
        public int value2;
    }
}
