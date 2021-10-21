package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * @author zz
 * @ClassName: Issue1804
 * @Description:
 * @date 2021/10/21 16:33
 */
public class Issue1804 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"name\":\"a\"}";
        PrivateConstructorClass privateConstructorClass = JSONObject.parseObject(json, PrivateConstructorClass.class);
    }

    public static class PrivateConstructorClass  {
        private String name;
        private PrivateConstructorClass(String name) {
            this.name = name;
        }

        public static PrivateConstructorClass valueOf(String name) {
            return new PrivateConstructorClass(name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
