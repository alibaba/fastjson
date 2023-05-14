package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Issue1750 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("Issue1750_ProcessBO");

        String json = "{\n" +
                "\t\"masterId\": \"1111111111111\",\n" +
                "\t\"processId\": \"222222222222222\",\n" +
                "\t\"taskId\": \"33333333333333\",\n" +
                "\t\"taskName\": \"44444444444444\"\n" +
                "}";
        Object obj = JSON.parseObject(json, clazz);
        String result = JSON.toJSONString(obj);
        System.out.println(result);
        assertEquals("{\"masterId\":\"1111111111111\",\"processId\":\"222222222222222\",\"taskId\":\"33333333333333\",\"taskName\":\"44444444444444\"}", result);
    }

    private static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/Issue1750_ProcessBO.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Issue1750_ProcessBO", bytes, 0, bytes.length);
            }
        }
    }
}
