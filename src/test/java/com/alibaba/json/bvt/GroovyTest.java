package com.alibaba.json.bvt;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class GroovyTest extends TestCase {

    public void test_groovy() throws Exception {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);

        // A类
        Class AClass = loader.parseClass("class A {\n" + //
                                         "    int id\n" + //
                                         "}");

        // A实例
        GroovyObject a = (GroovyObject) AClass.newInstance();
        a.setProperty("id", 33);
        System.out.println(JSON.toJSONString(a));
        System.out.println(a);

        // B类，继承于A
        Class BClass = loader.parseClass("class B extends A {\n" + //
        		"    String name\n" + //
        		"}");

        // B实例
        GroovyObject b = (GroovyObject) BClass.newInstance();
        System.out.println(b);

        // 序列化失败
        System.out.println(JSON.toJSONString(b, true));
    }
}
