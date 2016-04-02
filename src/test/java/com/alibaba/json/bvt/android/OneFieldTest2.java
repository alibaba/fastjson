package com.alibaba.json.bvt.android;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OneFieldTest2 extends TestCase {
    
    public void test_one_field() throws Exception {
        VO vo = new VO();
        vo.id = 1001;
        
        String text = JSON.toJSONString(vo);
        
        JSON.parseObject(text, VO.class);
    }

    public static class VO {

        public int id;

    }
}
