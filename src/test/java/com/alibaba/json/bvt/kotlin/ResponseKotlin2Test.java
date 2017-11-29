package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wenshao on 10/08/2017.
 */
public class ResponseKotlin2Test extends TestCase {
    public void test_kotlin() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("ResponseKotlin2");

        String json = "{\"text\":\"robohorse\",\"value\":99}";
        Object obj = JSON.parseObject(json, clazz);
        assertEquals("{\"text\":\"robohorse\",\"value\":99}", JSON.toJSONString(obj));

        String json2 = "{\"text\":\"robohorse\"}";
        Object obj2 = JSON.parseObject(json2, clazz);
        assertEquals("{\"text\":\"robohorse\"}", JSON.toJSONString(obj2));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/ResponseKotlin2.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("ResponseKotlin2", bytes, 0, bytes.length);
            }
        }
    }
}
