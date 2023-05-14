package com.alibaba.json.bvt.issue_3100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class Issue3109 extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig config = new ParserConfig();
        config.addAccept("test");
        JSON.parseObject("{\"@type\":\"testxx\",\"dogName\":\"dog1001\"}", Dog.class, config);
    }

    public static class Dog  {
        public String dogName;
    }
}
