package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wenshao on 06/08/2017.
 */
public class Class_WithPrimaryAndSecondaryConstructorTest extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("Class_WithPrimaryAndSecondaryConstructor");

        String json = "{\"name\":\"John Smith\",\"age\":30}";
        Object obj = JSON.parseObject(json, clazz);
        assertEquals("{\"age\":30,\"name\":\"John Smith\"}", JSON.toJSONString(obj));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/Class_WithPrimaryAndSecondaryConstructor.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Class_WithPrimaryAndSecondaryConstructor", bytes, 0, bytes.length);
            }
        }
    }
}
