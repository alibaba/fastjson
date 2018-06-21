package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 2016/10/22.
 */
public class PrivateConstrunctorTest extends TestCase {

    public void test_parse() throws Exception {
        JSON.parseObject("{}", Hidden.class);
    }

    public static class Hidden {
        private Hidden() {}

    }
}
