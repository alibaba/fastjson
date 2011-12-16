package com.alibaba.json.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class StackTraceElementDeserializerTest extends TestCase {

    public void test_stack() throws Exception {
        Assert.assertNull(JSON.parseObject("null", StackTraceElement.class));
        Assert.assertNull(JSON.parseArray("null", StackTraceElement.class));
        Assert.assertNull(JSON.parseArray("[null]", StackTraceElement.class).get(0));
        Assert.assertNull(JSON.parseObject("{\"value\":null}", VO.class).getValue());
        Assert.assertNull(JSON.parseObject("{\"className\":\"int\",\"methodName\":\"parseInt\"}", StackTraceElement.class).getFileName());
    }

    public static class VO {

        private StackTraceElement value;

        public StackTraceElement getValue() {
            return value;
        }

        public void setValue(StackTraceElement value) {
            this.value = value;
        }

    }
}
