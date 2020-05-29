package com.alibaba.fastjson;

import com.alibaba.fastjson.JSONPath;
import com.diffblue.deeptestutils.Reflector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class IssueTest {
    @Test
    public void testDeserialzeComplexGenericType() throws Exception {
        String s = "{\"props\": {\"test\": [{\"foo\": \"bar\"}]}}";
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("DataClassPropsGeneric");
        Object d = JSON.parseObject(s, clazz);
        System.out.println(d);
        Assert.assertNotNull(d);
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "kotlin/DataClassPropsGeneric.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("DataClassPropsGeneric", bytes, 0, bytes.length);
            }
        }
    }
}