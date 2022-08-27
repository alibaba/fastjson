package com.alibaba.json.bvt.parser.deser.list;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class ListFieldTest extends TestCase {
    public void test_for_list() throws Exception {
        JSON.parseObject("{'data':['a','b']}", TestPojo.class);
    }

    public static class TestPojo{
        public java.util.List<String> getData(){
            return null;
        }
    }
}
