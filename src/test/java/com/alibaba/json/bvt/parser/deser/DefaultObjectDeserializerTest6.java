package com.alibaba.json.bvt.parser.deser;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class DefaultObjectDeserializerTest6 extends TestCase {

    public void test_0() throws Exception {
        Entity vo = JSON.parseObject("{\"value\":{\"1\":{},\"2\":{\"$ref\":\"$.value.1\"}}}", Entity.class);
        Assert.assertSame(vo.getValue().get("1"), vo.getValue().get("2"));
    }
    
    public void test_1() throws Exception {
        Entity vo = JSON.parseObject("{\"value\":{\"1\":{},\"2\":{\"$ref\":\"..\"}}}", Entity.class);
        Assert.assertSame(vo.getValue(), vo.getValue().get("2"));
    }
    
    public static class Entity {

        private final Map<Object, Map<Object, Object>> value;

        @JSONCreator
        public Entity(@JSONField(name = "value") Map<Object, Map<Object, Object>> value){
            this.value = value;
        }

        public Map<Object, Map<Object, Object>> getValue() {
            return value;
        }

    }
}
