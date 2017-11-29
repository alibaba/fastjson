package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSONReader;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

import java.io.StringReader;

public class LongFieldTest_2 extends TestCase {

    public void test_min() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public void test_min_reader() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v);

        V0 v1 = new JSONReader(new StringReader(text)).readObject(V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_max() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MAX_VALUE);

        String text = JSON.toJSONString(v);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public void test_max_reader() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MAX_VALUE);

        String text = JSON.toJSONString(v);

        V0 v1 = new JSONReader(new StringReader(text)).readObject(V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_min_array() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v, SerializerFeature.BeanToArray);

        V0 v1 = JSON.parseObject(text, V0.class, Feature.SupportArrayToBean);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public void test_min_array_reader() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v, SerializerFeature.BeanToArray);

        V0 v1 = new JSONReader(new StringReader(text), Feature.SupportArrayToBean).readObject(V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_max_array() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MAX_VALUE);

        String text = JSON.toJSONString(v, SerializerFeature.BeanToArray);

        V0 v1 = JSON.parseObject(text, V0.class, Feature.SupportArrayToBean);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public void test_max_array_reader() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MAX_VALUE);

        String text = JSON.toJSONString(v, SerializerFeature.BeanToArray);

        V0 v1 = new JSONReader(new StringReader(text), Feature.SupportArrayToBean).readObject(V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public static class V0 {

        private Long value;

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

    }
}
