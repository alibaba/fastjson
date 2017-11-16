package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class NameAmbiguityTest extends TestCase {
    public void test_for_issue() throws Exception {
        String text= "";
        JSON.parseObject(text, Model.class);
    }

    public static class Model {
        public User organizer;
    }

    public static class User {

    }
}
