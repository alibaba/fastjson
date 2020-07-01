package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Issue2736 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject s = JSONObject.parseObject("{1:2,3:4}");
        for(String s1 : s.keySet()){
            System.out.println(s1);
        }
    }
}
