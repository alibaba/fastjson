package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class TestInnerClass2 extends TestCase {

    public void test_inner() throws Exception {
        VO vo = new VO(234);
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"value\":234}", text);

        Exception error = null;
        try {
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private class VO {

        private int value;

        public VO(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
