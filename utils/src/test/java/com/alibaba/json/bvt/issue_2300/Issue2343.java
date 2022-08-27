package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue2343 extends TestCase {
    public void test_for_issue() throws Exception {
        A a = new A();
        a.f1 = 101;
        a.f2 = 102;
        a.f3 = 103;

        String str = JSON.toJSONString(a);
        assertEquals("{\"f2\":102,\"f1\":101,\"f3\":103}", str);

        JSONObject object = JSON.parseObject(str);
        A a1 = object.toJavaObject(A.class);
        assertEquals(a.f1, a1.f1);
        assertEquals(a.f2, a1.f2);
        assertEquals(a.f3, a1.f3);
    }

    public static class A {
        @JSONField(ordinal = 1)
        public int f1;

        @JSONField(ordinal = 0)
        public int f2;

        @JSONField(ordinal = 2)
        public int f3;
    }
}
