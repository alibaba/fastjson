package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue3057 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\"q\":[]}";
        Bean bean = JSON.parseObject(str, Bean.class);
        assertEquals(0, bean.q.size());
    }

    public static class Bean {
        public java.util.Deque q;
    }
}
