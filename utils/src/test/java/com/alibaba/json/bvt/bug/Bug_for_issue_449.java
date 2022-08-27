package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class Bug_for_issue_449 extends TestCase {

    public void test_for_issue() throws Exception {
        ExaminationPojo vo = new ExaminationPojo();
        vo.setMg("1435555992");
        vo.setNa(" 02570");
        vo.setCl("150501");
        vo.setPanellot("150501");
        String text = JSON.toJSONString(vo);
        
        System.out.println(text);
        Assert.assertEquals("{\"Cl-\":\"150501\",\"Mg2+\":\"1435555992\",\"Na+\":\" 02570\",\"panellot\":\"150501\"}", text);
        
        ExaminationPojo v1 = JSON.parseObject(text, ExaminationPojo.class);
        Assert.assertEquals(vo.mg, v1.mg);
        Assert.assertEquals(vo.na, v1.na);
        Assert.assertEquals(vo.cl, v1.cl);
        Assert.assertEquals(vo.panellot, v1.panellot);
        
    }

    public static class ExaminationPojo {

        @JSONField(name = "Mg2+")
        private String mg;
        @JSONField(name = "Na+")
        private String na;
        @JSONField(name = "Cl-")
        private String cl;
        @JSONField(name = "panellot")
        private String panellot;
        
        public String getMg() {
            return mg;
        }
        
        public void setMg(String mg) {
            this.mg = mg;
        }
        
        public String getNa() {
            return na;
        }
        
        public void setNa(String na) {
            this.na = na;
        }
        
        public String getCl() {
            return cl;
        }
        
        public void setCl(String cl) {
            this.cl = cl;
        }
        
        public String getPanellot() {
            return panellot;
        }
        
        public void setPanellot(String panellot) {
            this.panellot = panellot;
        }
        

        
    }
}
