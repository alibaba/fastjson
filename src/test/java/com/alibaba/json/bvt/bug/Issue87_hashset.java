package com.alibaba.json.bvt.bug;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class Issue87_hashset extends TestCase {
    public void test_for_issue() throws Exception {
        TestObject to = new TestObject();
        to.add("test1");
        to.add("test2");
        String text = JSON.toJSONString(to);
        System.out.println(text);
        JSONObject jo = JSON.parseObject(text);
        to = JSON.toJavaObject(jo, TestObject.class);
    }
    
    public static class TestObject {

        private HashSet<String> set = new HashSet<String>(0);

        public HashSet<String> getSet() {
            return set;
        }

        public void setSet(HashSet<String> set) {
            this.set = set;
        }

        public void add(String str) {
            set.add(str);
        }
    }
}
