package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSONAware;

public class BugTest2 extends TestCase {

    public void test_0() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("a", new A());
        String text = obj.toString();

        System.out.println(text);
    }

    public static class A implements JSONAware {

        private int    id;
        private String name;

        private JSONObject toJSONObject() {
            JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("name", name);
            return json;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String toJSONString() {
            return toJSONObject().toString();
        }

    }
}
