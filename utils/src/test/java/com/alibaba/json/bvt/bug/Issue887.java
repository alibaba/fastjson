package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

/**
 * Created by wenshao on 2016/11/10.
 */
public class Issue887 extends TestCase {
    public void test_for_issue() throws Exception {
        Foo excepted = new Foo();
        excepted.setName("mock");
        String json;
        System.out.println(json = JSON.toJSONString(excepted, true));
        Foo actually = JSON.parseObject(json, Foo.class);
        assertEquals(excepted.getName(), actually.getName());
    }

    public static class Foo {
        @JSONField(name = "foo.name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
