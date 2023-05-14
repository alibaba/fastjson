package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;

public class DefaultExtJSONParserTest_6 extends TestCase {

    public void test_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{value:{,,,,\"value\":3,\"id\":1}}");
        parser.config(Feature.AllowArbitraryCommas, true);
        Entity entity = new Entity();
        parser.parseObject(entity);
        Assert.assertEquals(3, entity.getValue().getValue());
    }

    public void test_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{'value':{\"value\":3,\"id\":1}}");
        parser.config(Feature.AllowArbitraryCommas, false);
        Entity entity = new Entity();
        parser.parseObject(entity);
        Assert.assertEquals(3, entity.getValue().getValue());
    }

    public static class Entity {

        private V1 value;

        public V1 getValue() {
            return value;
        }

        public void setValue(V1 value) {
            this.value = value;
        }

    }

    public static class V1 {

        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
