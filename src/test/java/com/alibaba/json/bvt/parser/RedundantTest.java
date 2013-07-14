package com.alibaba.json.bvt.parser;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;

public class RedundantTest extends TestCase {

    public void test_extra() throws Exception {
        ExtraProcessor processor = new ExtraProcessor() {
            public void processExtra(Object object, String key, Object value) {
                VO vo = (VO) object;
                vo.getAttributes().put(key, value);
            }
        };

        VO vo = JSON.parseObject("{\"id\":123,\"name\":\"abc\"}", VO.class, processor);
        Assert.assertEquals(123, vo.getId());
        Assert.assertEquals("abc", vo.getAttributes().get("name"));
    }
    
    public void test_extraWithType() throws Exception {
        class MyExtraProcessor implements ExtraProcessor, ExtraTypeProvider {
            public void processExtra(Object object, String key, Object value) {
                VO vo = (VO) object;
                vo.getAttributes().put(key, value);
            }

            public Type getExtraType(Object object, String key) {
                if ("value".equals(key)) {
                    return int.class;
                }
                return null;
            }
        };
        ExtraProcessor processor = new MyExtraProcessor();

        VO vo = JSON.parseObject("{\"id\":123,\"value\":\"123456\"}", VO.class, processor);
        Assert.assertEquals(123, vo.getId());
        Assert.assertEquals(123456, vo.getAttributes().get("value"));
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
