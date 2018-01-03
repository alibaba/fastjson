package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Issue1569 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("Issue1569_User");

        String json = "{\"loginName\":\"san\",\"userId\":1}";
        Object head = JSON.parseObject(json, clazz);
        assertEquals(json, JSON.toJSONString(head));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/Issue1569_User.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Issue1569_User", bytes, 0, bytes.length);
            }
        }
    }
}
