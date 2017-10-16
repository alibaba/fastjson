package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1498 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = JSON.parseObject("{\"flag\":\"QUALITY_GRADUATE\"}", Model.class);
        assertNull(model.flag);
    }

    public void test_for_issue_match() throws Exception {
        Model model = JSON.parseObject("{\"flag\":\"IS_NEED_CHECK_IDENTITY\"}", Model.class);
        assertSame(BuFlag.IS_NEED_CHECK_IDENTITY, model.flag);
    }

    public static class Model {
        public BuFlag flag;
    }

    public enum BuFlag
    {
        IS_NEED_CHECK_IDENTITY(1L, "a"),  HAS_CHECK_IDENTITY(2L, "b");

        private long bit;
        private String desc;

        private BuFlag(long bit, String desc)
        {
            this.bit = bit;
            this.desc = desc;
        }

        public long getBit()
        {
            return this.bit;
        }
    }
}
