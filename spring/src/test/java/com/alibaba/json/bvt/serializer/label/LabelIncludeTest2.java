package com.alibaba.json.bvt.serializer.label;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.Labels;

import junit.framework.TestCase;

public class LabelIncludeTest2 extends TestCase {

    public void test_includes() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        vo.setName("wenshao");
        vo.setPassword("ooxxx");
        
        String text = JSON.toJSONString(vo, Labels.includes("normal"));
        Assert.assertEquals("{\"id\":123,\"name\":\"wenshao\"}", text);
    }

    private static class VO {

        private int    id;
        private String name;
        private String password;

        @JSONField(label = "normal")
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @JSONField(label = "normal")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JSONField(label = "secret")
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
