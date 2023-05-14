package com.alibaba.json.bvt.builder;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONType;

import junit.framework.TestCase;

public class BuilderTest_error extends TestCase {

    public void test_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"id\":12304,\"name\":\"ljw\"}", VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    @JSONType(builder = VOBuilder.class)
    public static class VO {

        private int    id;
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
            throw new IllegalStateException();
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
