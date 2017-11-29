package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteAsArray_float_public extends TestCase {
    public void test_0 () throws Exception {
        VO vo = new VO();
        vo.setId(123F);
        vo.setName("wenshao");
        
        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[123.0,\"wenshao\"]", text);
    }
    
    public static class VO {
        private float id;
        private String name;

        public float getId() {
            return id;
        }

        public void setId(float id) {
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
