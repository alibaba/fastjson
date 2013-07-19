package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONFieldTest5 extends TestCase {

    public void test_jsonField() throws Exception {
        VO vo = new VO();

        vo.setID(123);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"iD\":123}", text);
        
        Assert.assertEquals(123, JSON.parseObject(text, VO.class).getID());
    }

    public static class VO {

        private int id;

        public int getID() {
            return id;
        }

        public void setID(int id) {
            this.id = id;
        }

    }
}
