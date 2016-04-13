package com.alibaba.json.bvt.bug;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_383 extends TestCase {

    public void test_for_issue() throws Exception {
        TestClass ts = new TestClass();
        ts.getHashSet().add(1);
        ts.getHashSet().add(4);
        ts.getMember().getHashSet().add(10);
        ts.getMember().getHashSet().add(15);
        String jsonStr = JSON.toJSONString(ts, new SerializerFeature[]{
                SerializerFeature.WriteClassName, SerializerFeature.DisableCircularReferenceDetect
        });
        System.out.println(jsonStr);
        ts = JSON.parseObject(jsonStr, TestClass.class);
        for (Integer val : ts.getHashSet()) {
            System.out.println(val);
        }
    }

    public static class TestClass {

        private Set<Integer> hashSet = new HashSet<Integer>();
        private Member member = new Member();

        public TestClass() {
        }

        public Set<Integer> getHashSet() {
            return hashSet;
        }

        public void setHashSet(Set<Integer> hashSet) {
            this.hashSet = hashSet;
        }

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }
    }

    public static class Member{
        private Set<Integer> hashSet = new HashSet<Integer>();

        public Member() {
        }

        public Set<Integer> getHashSet() {
            return hashSet;
        }

        public void setHashSet(Set<Integer> hashSet) {
            this.hashSet = hashSet;
        }
    }
}
