package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * @Author ：Nanqi
 * @Date ：Created in 22:37 2020/7/2
 */
public class Issue3318 extends TestCase {
    public void test_for_issue() throws Exception {
        Simple simple = new Simple();
        simple.setAge(new BigDecimal("2.0"));
        String simpleJSON = JSON.toJSONString(simple);
        assertTrue(simpleJSON.contains(":2.0"));

        Simple simpleDes = JSON.parseObject(simpleJSON, Simple.class);
        assertTrue(simpleDes.getAge().equals(new BigDecimal("2.0")));
    }

    static class Simple {
        BigDecimal age;

        public BigDecimal getAge() {
            return age;
        }

        public void setAge(BigDecimal age) {
            this.age = age;
        }
    }
}
