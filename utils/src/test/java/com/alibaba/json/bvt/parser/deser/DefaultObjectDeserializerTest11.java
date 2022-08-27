package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.json.bvt.parser.deser.DefaultObjectDeserializerTest4.Entity;

public class DefaultObjectDeserializerTest11 extends TestCase {

    public void test_0() throws Exception {
        A a = new A();
        DefaultJSONParser parser = new DefaultJSONParser("{\"id\":123}", ParserConfig.getGlobalInstance());
        parser.parseObject(a);
    }

    public static class A {

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }
}
