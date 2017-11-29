package com.alibaba.json.bvt.ref;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class RefTest9 extends TestCase {

    public void test_bug_for_wanglin() throws Exception {
        VO vo = new VO();
        A a = new A();
        vo.setA(a);
        vo.getValues().add(a);

        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"a\":{},\"values\":[{\"$ref\":\"$.a\"}]}", text);
        
        VO vo2 = JSON.parseObject(text, VO.class);
    }

    public static class VO {

        private A      a;
        private Set<A> values = new HashSet<A>();

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

        public Set<A> getValues() {
            return values;
        }

        public void setValues(Set<A> values) {
            this.values = values;
        }

    }

    public static class A {

    }
}
