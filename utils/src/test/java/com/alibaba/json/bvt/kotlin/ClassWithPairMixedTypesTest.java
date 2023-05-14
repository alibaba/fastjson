package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wenshao on 06/08/2017.
 */
public class ClassWithPairMixedTypesTest extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("ClassWithPairMixedTypes");

        String json = "{\"person\":{\"first\":\"wenshao\",\"second\":99}}";
        Object obj = JSON.parseObject(json, clazz);
        assertEquals("{\"person\":{\"first\":\"wenshao\",\"second\":99}}", JSON.toJSONString(obj));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/ClassWithPairMixedTypes.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("ClassWithPairMixedTypes", bytes, 0, bytes.length);
            }
        }
    }
}
