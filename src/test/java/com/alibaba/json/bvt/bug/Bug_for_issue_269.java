package com.alibaba.json.bvt.bug;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_269 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{\"value\":\"2014-10-09T03:07:07.000Z\"}";
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(1412824027000L, vo.value.getTime());
    }

    public static class VO {

        private Date value;

        public Date getValue() {
            return value;
        }

        public void setValue(Date value) {
            this.value = value;
        }

    }
}
