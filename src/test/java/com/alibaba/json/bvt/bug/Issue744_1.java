package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

/**
 * Created by wenshao on 2016/11/13.
 */
public class Issue744_1 extends TestCase {

    public void test_for_issue() throws Exception {
        C c = new C();
        c.setName("name");

        String json = JSON.toJSONString(c);
        assertEquals("{}", json);
    }

    public static abstract class B {
        @JSONField(serialize = false, deserialize = false)
        public abstract String getName();

    }

    public static class C extends B {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
