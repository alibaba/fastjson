package com.alibaba.json.bvt.comparing_json_modules;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 25/03/2017.
 */
public class Invalid_Test extends TestCase {
    public void test_6_1() throws Exception {
        assertEquals(0, JSON.parse("+0"));
    }

//    public void test_6_5() throws Exception {
//        assertEquals(28, JSON.parse("034"));
//    }
}
