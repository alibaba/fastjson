package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest_compatibleWithJavaBean_boolean extends TestCase {

    private boolean origin_compatibleWithJavaBean;

    protected void setUp() throws Exception {
        origin_compatibleWithJavaBean = TypeUtils.compatibleWithJavaBean;
        TypeUtils.compatibleWithJavaBean = true;
    }

    protected void tearDown() throws Exception {
        TypeUtils.compatibleWithJavaBean = origin_compatibleWithJavaBean;
    }

    public void test_true() throws Exception {
        String text = JSON.toJSONString(new VO(true));
        Assert.assertEquals("{\"ID\":true}", text);
        Assert.assertEquals(true, JSON.parseObject(text, VO.class).isID());
    }

    public static class VO {

        private boolean id;

        public VO(){

        }

        public VO(boolean id){
            this.id = id;
        }

        public boolean isID() {
            return id;
        }

        public void setID(boolean id) {
            this.id = id;
        }

    }
}
