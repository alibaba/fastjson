package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by kimmking on 03/08/2017.
 */
public class Issue1369 extends TestCase {
    public void test_for_issue() throws Exception {
        Foo foo = new Foo();
        foo.a = 1;
        foo.b = "b";
        foo.bars = new Bar();
        foo.bars.c = 3;
        String json = JSON.toJSONString(foo);
        System.out.println(json);
        Assert.assertTrue(json.indexOf("\\")<0);
    }

    public static class Foo {
        public int a;
        public String b;
        public Bar bars;
    }
    public static class Bar {
        public int c;
    }
}
