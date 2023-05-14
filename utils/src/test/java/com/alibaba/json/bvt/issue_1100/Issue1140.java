package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;

/**
 * Created by wenshao on 11/04/2017.
 */
public class Issue1140 extends TestCase {
    public void test_for_issue() throws Exception {
        String s = "\uD83C\uDDEB\uD83C\uDDF7";

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JSON.writeJSONString(out, s);
    }
}
