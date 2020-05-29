package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Bug_for_huling extends TestCase {

    public void test_for_0() throws Exception {
        VO vo = new VO();
        vo.setValue("\0\0");
        
        Assert.assertEquals('\0', vo.getValue().charAt(0));
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        Assert.assertEquals("{\"value\":\"\\u0000\\u0000\"}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals("\0\0", vo2.getValue());
    }
    
    public void test_for_1() throws Exception {
        VO vo = new VO();
        vo.setValue("\1\1");
        
        Assert.assertEquals('\1', vo.getValue().charAt(0));
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        Assert.assertEquals("{\"value\":\"\\u0001\\u0001\"}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals("\1\1", vo2.getValue());
    }
    
    public void test_for_2028() throws Exception {
        VO vo = new VO();
        vo.setValue("\u2028\u2028");
        
        Assert.assertEquals('\u2028', vo.getValue().charAt(0));
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        Assert.assertEquals("{\"value\":\"\\u2028\\u2028\"}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals("\u2028\u2028", vo2.getValue());
    }

    public void test_for_2029() throws Exception {
        VO vo = new VO();
        vo.setValue("\u2029\u2029");

        Assert.assertEquals('\u2029', vo.getValue().charAt(0));

        String text = JSON.toJSONString(vo);
        System.out.println(text);
        Assert.assertEquals("{\"value\":\"\\u2029\\u2029\"}", text);

        VO vo2 = JSON.parseObject(text, VO.class);
        Assert.assertEquals("\u2029\u2029", vo2.getValue());
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
