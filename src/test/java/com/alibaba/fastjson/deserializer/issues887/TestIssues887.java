package com.alibaba.fastjson.deserializer.issues887;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by sqwen on 2016/11/8.
 */
public class TestIssues887 {

    @Test
    public void test() {
        Mock excepted = new Mock();
        excepted.setName("foo");
        Mock actually = JSON.parseObject(JSON.toJSONString(excepted, true), Mock.class);
        Assert.assertEquals(excepted.getName(), actually.getName());
    }

    public static class Mock {
        @JSONField(name = "mock.name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
