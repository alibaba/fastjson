package com.alibaba.json.bvt.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestProxy extends TestCase {

    public void test_0() throws Exception {
        Object vo = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {I.class}, new VO());
        
        String text = JSON.toJSONString(vo);
        
        System.out.println(text);
    }
    
    public static interface I {
        
    }
    

    public static class VO implements InvocationHandler {

        private int    id;
        private String name;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
        
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    
}
