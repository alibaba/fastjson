package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class Bug_for_issue_489 extends TestCase {

    public void test_for_issue() throws Exception {
        Foo ok = JSON.parseObject("{\"foo\":\"bar\"}", Foo.class);
        Foo ng = JSON.parseArray("[{\"foo\":\"bar\"}]").getObject(0, Foo.class);
    }

    public static final class Foo {

        public final String bar;

        @JSONCreator
        public Foo(@JSONField(name = "foo") final String bar){
            this.bar = bar;
        }
    }
}
