package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class GetSetNotMatchTest extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setValue(1);
        
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"value\":true}", text);
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getValue(), vo1.getValue());
    }

    public static class VO {

        private int value;

        public boolean getValue() {
            return value == 1;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
