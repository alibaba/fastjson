package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class Issue1524 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("DataClass");

        Constructor constructor = clazz.getConstructor(String.class, String.class);
        Object object = constructor.newInstance("ccc", "ddd");
        String json = JSON.toJSONString(object);
        assertEquals("{\"Id\":\"ccc\",\"Name\":\"ddd\"}", json);

        Object object2 = JSON.parseObject(json, clazz);
        String json2 = JSON.toJSONString(object2);
        assertEquals("{\"Id\":\"ccc\",\"Name\":\"ddd\"}", json2);
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/issue1526/DataClass.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("DataClass", bytes, 0, bytes.length);
            }
        }
    }
}
