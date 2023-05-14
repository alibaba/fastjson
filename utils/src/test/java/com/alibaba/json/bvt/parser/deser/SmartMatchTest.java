package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class SmartMatchTest extends TestCase {

    public void f_test_0() throws Exception {
        String text = "{\"message_id\":1001}";

        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(1001, vo.getMessageId());
    }

    public void test_vo2() throws Exception {
        String text = "{\"message_id\":1001}";

        VO2 vo = JSON.parseObject(text, VO2.class);
        Assert.assertEquals(1001, vo.getMessageId());
    }

    private static class VO {

        private int messageId;

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

    }

    public static class VO2 {

        private int messageId;

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

    }
}
