package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/03/2017.
 */
public class Issue1075 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{ \"question\": \"1+1=?\\u1505a\"}";
        JSON.parseObject(json);
    }
}
