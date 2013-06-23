package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class CharArrayDeserializerTest extends TestCase {

    public void test_charArray() throws Exception {
        Assert.assertEquals(null, JSON.parseObject("{}", VO.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", VO.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("{'value':null}", VO.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("{\"value\":null}", VO.class).getValue());
        Assert.assertEquals(0, JSON.parseObject("{\"value\":\"\"}", VO.class).getValue().length);
        Assert.assertEquals(2, JSON.parseObject("{\"value\":\"ab\"}", VO.class).getValue().length);
        Assert.assertEquals("ab", new String(JSON.parseObject("{\"value\":\"ab\"}", VO.class).getValue()));
        Assert.assertEquals("12", new String(JSON.parseObject("{\"value\":12}", VO.class).getValue()));
        Assert.assertEquals("12", new String(JSON.parseObject("{\"value\":12L}", VO.class).getValue()));
        Assert.assertEquals("12", new String(JSON.parseObject("{\"value\":12S}", VO.class).getValue()));
        Assert.assertEquals("12", new String(JSON.parseObject("{\"value\":12B}", VO.class).getValue()));
        Assert.assertEquals("{}", new String(JSON.parseObject("{\"value\":{}}", VO.class).getValue()));
    }

    public static class VO {

        private char[] value;

        public char[] getValue() {
            return value;
        }

        public void setValue(char[] value) {
            this.value = value;
        }

    }
}
