package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

/**
 * @Author ：Nanqi
 * @Date ：Created in 00:14 2020/7/18
 */
public class Issue3351 extends TestCase {
    public void test_for_issue() throws Exception {
        String cString = "c110";
        boolean cValid = JSONValidator.from(cString).validate();
        assertFalse(cValid);

        String jsonString = "{\"forecast\":\"sss\"}";
        boolean jsonValid = JSONValidator.from(jsonString).validate();
        assertTrue(jsonValid);
    }

}
