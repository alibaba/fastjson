package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import java.util.List;

public class Issue1429 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "[{\n" +
                "            \"@type\": \"com.alibaba.json.bvt.issue_1400.Issue1429$Student\",\n" +
                "            \"age\": 22,\n" +
                "            \"id\": 1,\n" +
                "            \"name\": \"hello\"\n" +
                "        }, {\n" +
                "            \"age\": 22,\n" +
                "            \"id\": 1,\n" +
                "            \"name\": \"hhh\",\n" +
                "            \"@type\": \"com.alibaba.json.bvt.issue_1400.Issue1429$Student\"\n" +
                "        }]";

        List list = JSON.parseArray(json);
        Student s0 = (Student) list.get(0);
        assertEquals(1, s0.id);
        assertEquals(22, s0.age);
        assertEquals("hello", s0.name);

        Student s1 = (Student) list.get(1);
        assertEquals(1, s1.id);
        assertEquals(22, s1.age);
        assertEquals("hhh", s1.name);
    }

    @JSONType
    public static class Student {
        public int id;
        public int age;
        public String name;
    }
}
