package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 19/12/2016.
 */
public class Bug_for_generic_huansi extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonStr = "{\"id\": 1234}";

        SimpleGenericObject jsonObj = JSON.parseObject(jsonStr, SimpleGenericObject.class);

        try {
            Long id = jsonObj.getId();
            assertTrue(id.equals(1234L));
        } catch (Exception e) {
            fail("parse error:" + e.getMessage());
        }
    }

    public static class BaseGenericType<T> {

        private T id;

        public T getId() {
            return id;
        }

        public void setId(T id) {
            this.id = id;
        }

    }

    public static class ExtendGenericType<T> extends BaseGenericType<T> {
    }

    public static class SimpleGenericObject extends ExtendGenericType<Long> {
    }

}
