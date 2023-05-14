package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ProxyTest extends TestCase {

    public void test_0() throws Exception {
        A a = create(A.class);
        a.setId(123);

        Assert.assertEquals("{\"id\":123}", JSON.toJSONString(a));
    }

    public static <T> T create(Class<T> classs) throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(classs);
        Class clazz = factory.createClass();
        MethodHandler handler = new MethodHandler() {

            public Object invoke(Object self, Method overridden, Method forwarder, Object[] args) throws Throwable {
                return forwarder.invoke(self, args);
            }
        };
        Object instance = clazz.newInstance();
        ((ProxyObject) instance).setHandler(handler);
        return (T) instance;
    }

    public static class A {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

}
