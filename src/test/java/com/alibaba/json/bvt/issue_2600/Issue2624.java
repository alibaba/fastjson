package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.StringCodec;
import junit.framework.TestCase;
import org.junit.Assert;

public class Issue2624 extends TestCase {
    public void test_String_Deserializer() throws Exception {
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.putDeserializer(String.class, Issue2624EfastStringCodec.EFAST_STRING_CODEC);
        parserConfig.putDeserializer(StringBuilder.class, Issue2624EfastStringCodec.EFAST_STRING_CODEC);
        parserConfig.putDeserializer(StringBuffer.class, Issue2624EfastStringCodec.EFAST_STRING_CODEC);

        String val = "{\"name\":\"\"}";

        //无配置，无注解
        Bean0 bean0 = JSON.parseObject(val, Bean0.class);
        Assert.assertEquals( "", bean0.name);

        //有配置
        Bean1 bean1 = JSON.parseObject(val, Bean1.class, parserConfig);
        Assert.assertEquals( null, bean1.name);

        //有注解
        Bean2 bean2 = JSON.parseObject(val, Bean2.class);
        Assert.assertEquals( null, bean2.name);

        //有配置，有注解 （以注解为准）
        Bean3 bean3 = JSON.parseObject(val, Bean3.class, parserConfig);
        Assert.assertEquals("", bean3.name);

    }

    static class Bean0 {
        public String name;
    }

    static class Bean1 {
        public String name;
    }

    static class Bean2 {
        @JSONField(deserializeUsing = Issue2624EfastStringCodec.class)
        public String name;
    }

    static class Bean3 {
        @JSONField(deserializeUsing = StringCodec.class)
        public String name;
    }
}


