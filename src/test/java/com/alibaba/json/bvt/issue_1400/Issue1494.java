package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

public class Issue1494 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"id\":1001,\"name\":\"wenshao\"}";
        B b = JSON.parseObject(json, B.class);
        assertEquals("{\"id\":1001,\"name\":\"wenshao\"}", JSON.toJSONString(b));
    }

    public static class A {
        private int id;

        public int getId() {
            return id;
        }
    }

    @JSONType(parseFeatures = Feature.SupportNonPublicField)
    public static class B extends A {
        private String name;

        public String getName() {
            return name;
        }
    }
}
