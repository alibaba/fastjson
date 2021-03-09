package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class Issue3672 {
    @Test
    public void test1() {
        Issue3672Root root = new Issue3672Root();
        Issue3672A a = new Issue3672A();
        Issue3672B b = new Issue3672B();
        Issue3672C c = new Issue3672C();
        Issue3672D d = new Issue3672D();
        root.setA(a);
        a.setB(Lists.newArrayList(b).toArray());
        b.setC(c);
        c.setD(d);
        d.setE(Lists.newArrayList(c));
        Assert.assertFalse(JSON.toJSONString(root).contains("null"));
        Assert.assertFalse(JSON.toJSONString(root, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue).contains("null"));
    }

    @Test
    public void test2() {
        Issue3672Root2 root = new Issue3672Root2();
        Issue3672A2 a = new Issue3672A2();
        Issue3672B b = new Issue3672B();
        Issue3672C c = new Issue3672C();
        Issue3672D d = new Issue3672D();
        root.setA(a);
        a.setB(Lists.newArrayList(b).toArray(new Issue3672B[0]));
        b.setC(c);
        c.setD(d);
        d.setE(Lists.newArrayList(c));
        Assert.assertFalse(JSON.toJSONString(root).contains("null"));
        Assert.assertFalse(JSON.toJSONString(root, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue).contains("null"));
    }

    @Data
    private class Issue3672Root {
        private Issue3672A a;
    }

    @Data
    private class Issue3672Root2 {
        private Issue3672A2 a;
    }

    @Data
    private class Issue3672A {
        private Object[] b;
    }

    @Data
    private class Issue3672A2 {
        private Issue3672B[] b;
    }

    @Data
    private class Issue3672B {
        private Issue3672C c;
    }

    @Data
    private class Issue3672C {
        private Issue3672D d;
    }

    @Data
    private class Issue3672D {
        private ArrayList<Issue3672C> e;
    }

}
