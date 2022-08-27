package com.alibaba.json.bvt.builder;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;

import junit.framework.TestCase;

public class BuilderTest2 extends TestCase {
    
    public void test_create() throws Exception {
        VO vo = JSON.parseObject("{\"id\":12304,\"name\":\"ljw\"}", VO.class);
        
        Assert.assertEquals(12304, vo.getId());
        Assert.assertEquals("ljw", vo.getName());
    }

    @JSONType(builder=VOBuilder.class)
    public static class VO {
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
    }

    @JSONPOJOBuilder(buildMethod="xxx")
    public static class VOBuilder {

        private VO vo = new VO();

        public VO xxx() {
            return vo;
        }
        
        public VOBuilder withId(int id) {
            vo.id = id;
            return this;
        }
        
        public VOBuilder withName(String name) {
            vo.name = name;
            return this;
        }
    }
}
