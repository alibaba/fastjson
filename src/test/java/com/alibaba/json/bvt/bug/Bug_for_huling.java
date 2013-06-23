package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Bug_for_huling extends TestCase {

    public void test_for_objectKey() throws Exception {
        VO vo = new VO();
        vo.setValue("\0\0");
        
        Assert.assertEquals('\0', vo.getValue().charAt(0));
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        Assert.assertEquals("{\"value\":\"\\0\\0\"}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals("\0\0", vo2.getValue());
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
