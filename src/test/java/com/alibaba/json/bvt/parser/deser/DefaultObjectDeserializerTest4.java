package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

public class DefaultObjectDeserializerTest4 extends TestCase {

    public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{\"id\":3, \"name\":\"xx\"}", ParserConfig.getGlobalInstance());

        Entity entity = new Entity();
        parser.parseObject(entity);
    }

    public void test_1() throws Exception {
        JSON.parseObject("{\"id\":3, \"name\":\"xx\"}", Entity.class, 0, Feature.IgnoreNotMatch);
    }

    public static class Entity {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
