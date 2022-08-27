package com.alibaba.json.bvt.writeAsArray;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class WriteAsArray_double_private extends TestCase {
    public void test_0 () throws Exception {
        VO vo = new VO();
        vo.setId(123D);
        vo.setName("wenshao");
        
        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[123.0,\"wenshao\"]", text);
        
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertTrue(vo.id == vo2.id);
        Assert.assertEquals(vo.name, vo2.name);
    }
    
    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[123.A,\"wenshao\"]", VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[\"A\",\"wenshao\"]", VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[123:\"wenshao\"]", VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public static class VO {
        private double id;
        private String name;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
