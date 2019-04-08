package com.alibaba.fastjson.deserializer.issue2358;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by liangchuyi on 2019/4/8.
 */
public class TestJson {

    private String test1;
    private String test2;

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    class TestJson2 {
        private String test1;
        private String test2;

        public String getTest1() {
            return test1;
        }

        public void setTest1(String test1) {
            this.test1 = test1;
        }

        public String getTest2() {
            return test2;
        }

        public void setTest2(String test2) {
            this.test2 = test2;
        }
    }

    public static void main(String args[]) {
        String str = "[{\n" +
                "  \"test1\":\"1\",\n" +
                "  \"test2\":\"2\"\n" +
                "},\n" +
                " {\n" +
                "   \"test1\":\"1\",\n" +
                "   \"test2\":\"2\"\n" +
                " }]";
        List<TestJson2> testJsons = JSONObject.parseArray(str, TestJson2.class);
        System.out.println(testJsons);
    }
}
