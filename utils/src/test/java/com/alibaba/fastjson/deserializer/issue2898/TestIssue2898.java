package com.alibaba.fastjson.deserializer.issue2898;

import java.io.IOException;
import java.io.InputStream;

import com.alibaba.fastjson.JSON;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestIssue2898 {
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