package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Issue1420 extends TestCase {
    public void test_for_issue() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("A");

        String json = "{\"id\":1,\"name\":\"a\"}";
        Object obj = JSON.parseObject(json, clazz);
        assertEquals("{\"id\":1,\"name\":\"a\"}", JSON.toJSONString(obj));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/A.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("A", bytes, 0, bytes.length);
            }
        }
    }
}
