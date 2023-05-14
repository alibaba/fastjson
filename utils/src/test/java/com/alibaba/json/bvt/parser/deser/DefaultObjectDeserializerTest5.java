package com.alibaba.json.bvt.parser.deser;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class DefaultObjectDeserializerTest5 extends TestCase {

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[]", new TypeReference<Map<Object, Object>>() {
            });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject(",]", new TypeReference<Map<Object, Object>>() {
            });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[{},{\"$ref\":0}]",
                             new TypeReference<List<Map<Object, Object>>>() {
                             });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[{},{\"$ref\":\"$[0]\",}]",
                             new TypeReference<List<Map<Object, Object>>>() {
                             });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_0() throws Exception {
        List<Map<Object, Object>> list = JSON.parseObject("[{},{\"$ref\":\"$[0]\"}]",
                                                          new TypeReference<List<Map<Object, Object>>>() {
                                                          });
        Assert.assertSame(list.get(0), list.get(1));
    }

    public void test_1() throws Exception {
        Map<Object, Map<Object, Object>> map = JSON.parseObject("{\"1\":{},\"2\":{\"$ref\":\"$\"}}",
                                                                new TypeReference<Map<Object, Map<Object, Object>>>() {
                                                                });
        Assert.assertSame(map, map.get("2"));
    }

    public void test_2() throws Exception {
        Map<Object, Map<Object, Object>> map = JSON.parseObject("{\"1\":{},\"2\":{\"$ref\":\"..\"}}",
                                                                new TypeReference<Map<Object, Map<Object, Object>>>() {
                                                                });
        Assert.assertSame(map, map.get("2"));
    }

  
}
