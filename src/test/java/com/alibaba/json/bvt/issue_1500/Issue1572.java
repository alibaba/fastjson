package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.util.Map;
import java.util.Set;

public class Issue1572 extends TestCase {
    public void test_for_issue() throws Exception {
        Person person = new Person();

        person.setId("1001");

        person.setName("1001");

        Map<String, Object> pathValues = JSONPath.paths(person);
        Set<String> paths = pathValues.keySet();

        assertEquals(3, paths.size());
        assertEquals("1001", pathValues.get("/id"));
        assertEquals("1001", pathValues.get("/name"));
        assertSame(person, pathValues.get("/"));
    }

    public static class Person {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
