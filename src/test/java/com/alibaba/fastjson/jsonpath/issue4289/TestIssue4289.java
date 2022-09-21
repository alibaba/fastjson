package com.alibaba.fastjson.jsonpath.issue4289;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestIssue4289 {

    @Test
    public void TestIssue4289(){
        String json = "{\n" +
                "    \"name\": \"a\",\n" +
                "    \"list\": [\n" +
                "        {\n" +
                "            \"name\": \"b1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"b2\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        A a = JSON.parseObject(json,A.class);
        List<String> list = (List<String>) JSONPath.eval(a, "$..name");
        Assert.assertEquals(list.size(), 3);
    }


    public static class A {
        private String name;

        private List<B> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<B> getList() {
            return list;
        }

        public void setList(List<B> list) {
            this.list = list;
        }
    }

    public static class B {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
