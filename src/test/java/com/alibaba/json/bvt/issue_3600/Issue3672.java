package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;

import junit.framework.TestCase;

public class Issue3672 extends TestCase {
    public void test_for_issue() throws Exception {
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
        String str1 = JSON.toJSONString(root, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        String str2 = JSON.toJSONString(root);

        JSONObject obj1 = JSON.parseObject(str1);
        JSONObject obj2 = JSON.parseObject(str2);
        assertEquals(obj1.toString(), obj2.toString());
    }

    @Data
    private class Issue3672Root {
        private Issue3672A a;
    }

    @Data
    private class Issue3672A {
        private Object[] b;
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
