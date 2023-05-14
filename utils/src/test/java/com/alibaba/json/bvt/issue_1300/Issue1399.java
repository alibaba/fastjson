package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 15/08/2017.
 */
public class Issue1399 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("false", boolean.class);
        JSON.parseObject("false", Boolean.class);
        JSON.parseObject("\"false\"", boolean.class);
        JSON.parseObject("\"false\"", Boolean.class);

//        JSON.parseObject("FALSE", boolean.class);
//        JSON.parseObject("FALSE", Boolean.class);
        JSON.parseObject("\"FALSE\"", boolean.class);
        JSON.parseObject("\"FALSE\"", Boolean.class);
    }

    public void test_for_issue_true() throws Exception {
        JSON.parseObject("true", boolean.class);
        JSON.parseObject("true", Boolean.class);
        JSON.parseObject("\"true\"", boolean.class);
        JSON.parseObject("\"true\"", Boolean.class);

//        JSON.parseObject("FALSE", boolean.class);
//        JSON.parseObject("FALSE", Boolean.class);
        JSON.parseObject("\"TRUE\"", boolean.class);
        JSON.parseObject("\"TRUE\"", Boolean.class);
    }
}
