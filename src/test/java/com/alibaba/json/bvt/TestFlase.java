package com.alibaba.json.bvt;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestFlase extends TestCase {

    public void test_0() throws Exception {
        Object obj = JSON.parseObject("[{\"data\":{\"@type\":\"java.util.TreeMap\",false:21L},\"dataType\":2,\"dis\":0,\"length\":24,\"maxNum\":100,\"size\":3600000,\"version\":0}]", VO[].class);
        System.out.println(JSON.toJSONString(obj));
    }

    public static class VO {

        private Object data;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }
}
