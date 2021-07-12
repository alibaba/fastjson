package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;

/**
 * @Author ：Nanqi
 * @Date ：Created in 23:13 2020/7/6
 */
public class Issue3330 extends TestCase {
    @Override
    protected void setUp() throws Exception {
        System.setProperty(IOUtils.FASTJSON_BROWSERUNICODEWITHLOWERCASE, "true");
        IOUtils.dealBrowserUnicodeWithLowerCase();
    }

    @Override
    protected void tearDown() throws Exception {
        System.setProperty(IOUtils.FASTJSON_BROWSERUNICODEWITHLOWERCASE, "false");
        IOUtils.dealBrowserUnicodeWithLowerCase();
    }

    public void test_for_issue() throws Exception {
        Simple simple = new Simple();
        simple.setName("我是中文");
        String simpleJsonString = JSON.toJSONString(simple, SerializerFeature.BrowserCompatible);
        assertEquals(simpleJsonString, "{\"name\":\"\\u6211\\u662f\\u4e2d\\u6587\"}");
    }

    static class Simple {
        private String name;

        private Integer age;

        private String test;

        private String info;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
