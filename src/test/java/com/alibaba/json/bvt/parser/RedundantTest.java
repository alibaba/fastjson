package com.alibaba.json.bvt.parser;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;

public class RedundantTest extends TestCase {

    public void test_0() throws Exception {
        ExtraProcessor processor = new ExtraProcessor() {
            public void process(Object object, String key, Object value) {
                VO vo = (VO) object;
                vo.getAttributes().put(key, value);
            }
        };

        VO vo = JSON.parseObject("{\"id\":123,\"name\":\"abc\"}", VO.class, processor);
        Assert.assertEquals(123, vo.getId());
        Assert.assertEquals("abc", vo.getAttributes().get("name"));
    }

    public static class VO {

        private int                 id;
        private Map<String, Object> attributes = new HashMap<String, Object>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

    }
}
