package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.sql.Timestamp;

public class Issue3637 extends TestCase {
    public void test_for_issue() throws Exception {
//        java.sql.Time.valueOf("01:00:00");
        JSON.parseObject("\"01:00:00\"", java.sql.Time.class);
        TypeUtils.castToJavaBean("01:00:00", java.sql.Time.class);
    }
}
