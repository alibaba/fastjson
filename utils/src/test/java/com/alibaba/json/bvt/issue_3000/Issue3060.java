package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue3060 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\"type\":1}";
        Bean bean = JSON.parseObject(str).toJavaObject(Bean.class);
        Bean bean1 = JSON.parseObject(str, Bean.class);
        assertEquals(bean.type, bean1.type);
    }

    public static class Bean {
        public int type;
    }

    public enum Type {
        Small, Big
    }
}
