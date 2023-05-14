package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.LongFieldTest_2.V0;

import junit.framework.TestCase;

public class LongFieldTest_2_private extends TestCase {

    public void test_min() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_max() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_min_array() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v, SerializerFeature.BeanToArray);

        V0 v1 = JSON.parseObject(text, V0.class, Feature.SupportArrayToBean);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_max_array() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);

        String text = JSON.toJSONString(v, SerializerFeature.BeanToArray);

        V0 v1 = JSON.parseObject(text, V0.class, Feature.SupportArrayToBean);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    private static class V0 {

        private Long value;

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

    }
}
