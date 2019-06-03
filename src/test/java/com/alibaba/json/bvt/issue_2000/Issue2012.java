package com.alibaba.json.bvt.issue_2000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue2012 extends TestCase {
    public void test_for_issue() throws Exception {
        Model foo = new Model();
        foo.bytes = new byte[0];
        String str = JSON.toJSONString(foo, SerializerFeature.WriteClassName);
        assertEquals("{\"@type\":\"com.alibaba.json.bvt.issue_2000.Issue2012$Model\",\"bytes\":x''}", str);

        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);
        foo = JSON.parseObject(str, Object.class,config);
        assertEquals(0, foo.bytes.length);
    }

    public static class Model {
        public byte[] bytes;
    }
}
