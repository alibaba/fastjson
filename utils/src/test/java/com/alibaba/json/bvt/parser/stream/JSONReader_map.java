package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_map extends TestCase {

    public void test_array() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[{\"id\":123}]"));

        reader.startArray();

        Map<String, Object> map = new HashMap<String, Object>();
        reader.readObject(map);

        Assert.assertEquals(123, map.get("id"));

        reader.endArray();

        reader.close();
    }

    public void test_map() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"id\":123}"));

        Map<String, Object> map = new HashMap<String, Object>();
        reader.readObject(map);

        Assert.assertEquals(123, map.get("id"));

        reader.close();
    }
}
