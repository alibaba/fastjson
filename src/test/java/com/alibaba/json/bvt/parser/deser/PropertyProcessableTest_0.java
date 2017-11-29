package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.PropertyProcessable;
import junit.framework.TestCase;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 15/07/2017.
 */
public class PropertyProcessableTest_0 extends TestCase {
    public void test_processable() throws Exception {
        VO vo = JSON.parseObject("{\"vo_id\":123,\"vo_name\":\"abc\",\"value\":{}}", VO.class);
        assertEquals(123, vo.id);
        assertEquals("abc", vo.name);
        assertNotNull(vo.value);
    }

    public static class VO implements PropertyProcessable {
        public int id;
        public String name;
        public Value value;

        public Type getType(String name) {
            if ("value".equals(name)) {
                return Value.class;
            }
            return null;
        }

        public void apply(String name, Object value) {
            if ("vo_id".equals(name)) {
                this.id = ((Integer) value).intValue();
            } else if ("vo_name".equals(name)) {
                this.name = (String) value;
            } else if ("value".equals(name)) {
                this.value = (Value) value;
            }
        }
    }

    public static class Value {

    }
}
