package com.alibaba.json.bvt;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.junit.Assert;
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
        String textA = JSON.toJSONString(a);
        
        GroovyObject aa = (GroovyObject) JSON.parseObject(textA, AClass);
        Assert.assertEquals(a.getProperty("id"), aa.getProperty("id"));
        
        System.out.println(a);

        // B类，继承于A
        Class BClass = loader.parseClass("class B extends A {\n" + //
        		"    String name\n" + //
        		"}");

        // B实例
        GroovyObject b = (GroovyObject) BClass.newInstance();
        b.setProperty("name", "jobs");
        String textB = JSON.toJSONString(b);
        GroovyObject bb = (GroovyObject) JSON.parseObject(textB, BClass);
        Assert.assertEquals(b.getProperty("id"), bb.getProperty("id"));
        Assert.assertEquals(b.getProperty("name"), bb.getProperty("name"));
        

        // 序列化失败
        System.out.println(JSON.toJSONString(b, true));
    }
}
