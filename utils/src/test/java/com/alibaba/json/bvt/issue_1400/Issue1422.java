package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import junit.framework.TestCase;

import java.io.StringReader;

public class Issue1422 extends TestCase {
    public void test_for_issue() throws Exception {
        String strOk = "{\"v\": 111}";

        Foo ok = JSON.parseObject(strOk, Foo.class);
        assertFalse(ok.v);
    }

    public void test_for_issue_reader() throws Exception {
        String strBad = "{\"v\": 111}";
        Foo bad = new JSONReader(new StringReader(strBad)).readObject(Foo.class);
        assertFalse(bad.v);
    }

    public void test_for_issue_1() throws Exception {
        String strBad = "{\"v\":111}";
        Foo bad = JSON.parseObject(strBad, Foo.class);
        assertFalse(bad.v);
    }

    public void test_for_issue_1_reader() throws Exception {
        String strBad = "{\"v\":111}";
        Foo bad = new JSONReader(new StringReader(strBad)).readObject(Foo.class);
        assertFalse(bad.v);
    }

    public static class Foo {
        public boolean v;
    }
}
