package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wenshao on 10/08/2017.
 */
public class KUserTest extends TestCase {
    public void test_kotlin() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("com.alibaba.perf.KUser");

        String json = "{\"name\":\"robohorse\",\"age\":99}";
        Object obj = JSON.parseObject(json, clazz);
        assertEquals("{\"age\":99,\"name\":\"robohorse\"}", JSON.toJSONString(obj));

        String json2 = "{\"name\":\"robohorse\"}";
        Object obj2 = JSON.parseObject(json2, clazz);
        assertEquals("{\"age\":0,\"name\":\"robohorse\"}", JSON.toJSONString(obj2));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/KUser.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("com.alibaba.perf.KUser", bytes, 0, bytes.length);
            }
        }
    }
}
