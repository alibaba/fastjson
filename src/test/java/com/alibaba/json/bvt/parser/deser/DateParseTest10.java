package com.alibaba.json.bvt.parser.deser;

import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DateParseTest10 extends TestCase {

    public void test_date() throws Exception {
        String text = "{\"value\":\"1979-07-14\"}";
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.getValue(), new SimpleDateFormat("yyyy-MM-dd").parse("1979-07-14").getTime());
    }

    public static class VO {

        private long value;

        public long getValue() {
            return value;
        }

        public VO setValue(long value) {
            this.value = value;
            return this;
        }

    }
}
