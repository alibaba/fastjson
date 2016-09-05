package com.alibaba.json.bvt.parser.creator;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class JSONCreatorTest7 extends TestCase {

    public void test_create() throws Exception {
        Entity entity = JSON.parseObject("{\"values\":[{}]}", Entity.class);
        Assert.assertEquals(1, entity.values.size());
        Assert.assertEquals(Value.class, entity.values.get(0).getClass());
    }


    public static class Entity {

        private final List<Value> values;

        @JSONCreator
        public Entity(@JSONField(name = "values") List<Value> values){
            this.values = values;
        }

        public List<Value> getValues() {
            return values;
        }

    }

    public static class Value {

    }
}
