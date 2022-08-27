package com.alibaba.json.bvt.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class JSONTypeAutoTypeCheckHandlerTest extends TestCase {
    public void test_for_checkAutoType() throws Exception {
        Cat cat = (Cat) JSON.parseObject("{\"@type\":\"Cat\",\"catId\":123}", Animal.class);
        assertEquals(123, cat.catId);
    }

    @JSONType(autoTypeCheckHandler = MyAutoTypeCheckHandler.class)
    public static class Animal {

    }

    public static class Cat extends Animal {
        public int catId;
    }

    public static class Mouse extends Animal {

    }

    public static class MyAutoTypeCheckHandler implements ParserConfig.AutoTypeCheckHandler {

        public Class<?> handler(String typeName, Class<?> expectClass, int features) {
            if ("Cat".equals(typeName)) {
                return Cat.class;
            }

            if ("Mouse".equals(typeName)) {
                return Mouse.class;
            }

            return null;
        }
    }
}
