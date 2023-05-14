package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

public class TestSpecial6 extends TestCase {

    public void test_1() throws Exception {

        VO vo = new VO();
        vo.setValue("马䶮");

        String text = JSON.toJSONString(vo);
        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals(vo.value, vo2.value);

        assertEquals("{\"value\":\"马䶮\"}", text);
    }

    public static class VO {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
