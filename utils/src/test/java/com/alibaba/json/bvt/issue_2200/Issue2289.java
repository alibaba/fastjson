package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import junit.framework.TestCase;

public class Issue2289 extends TestCase {
    public void test_for_issue() throws Exception {
        B b = new B();
        b.id = 123;

        JSONSerializer jsonSerializer = new JSONSerializer();

        jsonSerializer.writeAs(b, A.class);

        String str = jsonSerializer.toString();
        assertEquals("{}", str);
    }

    public static class A {

    }

    public static class B extends A {
        public int id;
    }
}
