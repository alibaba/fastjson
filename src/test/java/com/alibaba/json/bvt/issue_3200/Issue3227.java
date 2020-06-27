package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @Author ：Nanqi
 * @Date ：Created in 20:38 2020/6/27
 */
public class Issue3227 extends TestCase {
    public void test_for_issue() {
        String json = "{\"code\":\"123\"}";
        if (!Child.class.getMethods()[0].getReturnType().getName().contains("Object")) {
            System.out.println(Child.class.getMethods()[0].getReturnType().getName());
        }
        Child child = JSON.parseObject(json, Child.class);
        Assert.assertNotNull(child);
    }

    static class Parent<T> {
        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        protected T code;

        public T getCode() {
            return code;
        }

        public void setCode(T code) {
            this.code = code;
        }
    }

    static class Child extends Parent<Integer>{
        @Override
        public Integer getCode() {
            return code;
        }

        @Override
        public void setCode(Integer code) {
            this.code = code;
        }
    }
}
