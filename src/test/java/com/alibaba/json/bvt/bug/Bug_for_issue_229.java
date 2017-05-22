package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_229 extends TestCase {

    public void test_for_issue() throws Exception {
        Assert.assertTrue(JSON.parseObject("{\"value\":1}", VO.class).value);
        Assert.assertFalse(JSON.parseObject("{\"value\":0}", VO.class).value);
    }

    public static class VO {

        private boolean value;

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

    }
}
