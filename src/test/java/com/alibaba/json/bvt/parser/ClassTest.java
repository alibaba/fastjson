package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class ClassTest extends TestCase {

    public void test_class_array() throws Exception {
        String text = "{\"clazz\":\"[Ljava.lang.String;\",\"value\":\"[\\\"武汉银行\\\"]\"}";
        
        VO vo = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(String[].class, vo.getClazz());
    }
    
    public void test_class() throws Exception {
        String text = "{\"clazz\":\"Ljava.lang.String;\",\"value\":\"[\\\"武汉银行\\\"]\"}";
        
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(String.class, vo.getClazz());
    }

    public static class VO {

        private Class<?> clazz;
        private Object   value;

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }
}
