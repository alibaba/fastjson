package com.alibaba.json.bvt.parser.deser.asm;

import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestASM_null extends TestCase {

    public void test_null() throws Exception {
        List<VO> list = JSON.parseArray("[{\"f1\":\"v1\",\"f2\":\"v2\"},{\"f2\":\"v2\",\"f3\":\"v3\"},{\"f2\":\"v2\",\"f3\":\"v3\"},{\"f1\":\"v1\",\"f3\":\"v3\"}]", VO.class);
        String text = JSON.toJSONString(list, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("[{'f1':'v1','f2':'v2'},{'f2':'v2','f3':'v3'},{'f2':'v2','f3':'v3'},{'f1':'v1','f3':'v3'}]", text);
//        System.out.println(text);
    }
    
    public void test_null_notmatch() throws Exception {
        List<VO> list = JSON.parseArray("[{\"f3\":\"v3\",\"f2\":\"v2\",\"f1\":\"v1\"}]", VO.class);
        String text = JSON.toJSONString(list, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("[{'f1':'v1','f2':'v2','f3':'v3'}]", text);
//        System.out.println(text);
    }
    

    public static class VO {

        private String f1;
        private String f2;
        private String f3;

        public VO(){

        }

        public VO(String f1, String f2, String f3){
            super();
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
        }

        public String getF1() {
            return f1;
        }

        public void setF1(String f1) {
            this.f1 = f1;
        }

        public String getF2() {
            return f2;
        }

        public void setF2(String f2) {
            this.f2 = f2;
        }

        public String getF3() {
            return f3;
        }

        public void setF3(String f3) {
            this.f3 = f3;
        }

    }
}
