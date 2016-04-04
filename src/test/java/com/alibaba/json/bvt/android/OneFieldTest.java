package com.alibaba.json.bvt.android;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OneFieldTest extends TestCase {
    
    public void test_one_field() throws Exception {
        VO vo = new VO();
        vo.setId(1001);
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        JSON.parseObject(text, VO.class);
    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
