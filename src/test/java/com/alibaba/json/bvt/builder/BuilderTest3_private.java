package com.alibaba.json.bvt.builder;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import junit.framework.TestCase;

public class BuilderTest3_private extends TestCase {
    
    public void test_create() throws Exception {
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

    private static class VOBuilder {

        private VO vo = new VO();

        public VO create() {
            return vo;
        }
        
        @JSONField(name="id")
        public VOBuilder kkId(int id) {
            vo.id = id;
            return this;
        }
        
        @JSONField(name="name")
        public VOBuilder kkName(String name) {
            vo.name = name;
            return this;
        }
    }
}
