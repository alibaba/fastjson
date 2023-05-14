package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest_compatibleWithJavaBean extends TestCase {

    private boolean origin_compatibleWithJavaBean;

    protected void setUp() throws Exception {
        origin_compatibleWithJavaBean = TypeUtils.compatibleWithJavaBean;
        TypeUtils.compatibleWithJavaBean = true;
    }

    protected void tearDown() throws Exception {
        TypeUtils.compatibleWithJavaBean = origin_compatibleWithJavaBean;
    }

    public void test_true() throws Exception {
        String text = JSON.toJSONString(new VO(123));
        Assert.assertEquals("{\"ID\":123}", text);
        Assert.assertEquals(123, JSON.parseObject(text, VO.class).getID());
    }

    public static class VO {

        private int id;

        public VO(){

        }

        public VO(int id){
            this.id = id;
        }

        public int getID() {
            return id;
        }

        public void setID(int id) {
            this.id = id;
        }

    }
}
