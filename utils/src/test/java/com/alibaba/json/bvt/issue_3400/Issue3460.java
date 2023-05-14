package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.json.bvt.issue_3200.Issue3293;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Description:  <br>
 *
 * @author byw
 * @create 2020/9/20
 */
public class Issue3460 extends TestCase {

    public void test_for_issue() throws Exception {
        String body = "11{\"time\":" + System.currentTimeMillis() + "}";

        assertFalse(
                JSONValidator.from(body)
                        .validate());

        assertTrue(
                JSONValidator.from(body)
                        .setSupportMultiValue(true)
                        .validate());
    }

}