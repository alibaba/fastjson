package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 08/05/2017.
 */
public class Issue1153 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\n" +
                "name: 'zhangshan', //这是一个姓名\n" +
                "test : '//helo'\n" +
                "}";

        JSON.parseObject(json);
    }
}
