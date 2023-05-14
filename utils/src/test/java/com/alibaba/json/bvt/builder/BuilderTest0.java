package com.alibaba.json.bvt.builder;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;

import junit.framework.TestCase;

public class BuilderTest0 extends TestCase {
    
    public void test_0() throws Exception {
        VO vo = JSON.parseObject("{\"id\":12304,\"name\":\"ljw\"}", VO.class);
        
        Assert.assertEquals(12304, vo.getId());
        Assert.assertEquals("ljw", vo.getName());
    }

    @JSONType(builder=VOBuilder.class)
    public static class VO {
        private int id;
        private String name;
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
    }

    public static class VOBuilder {

        private VO vo = new VO();

        public VO build() {
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
