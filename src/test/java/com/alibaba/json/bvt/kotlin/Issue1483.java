package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wenshao on 05/08/2017.
 */
public class Issue1483 extends TestCase {

    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("Person");

        String json = "{\"age\":99,\"name\":\"robohorse\",\"desc\":\"xx\"}";
        Object obj = JSON.parseObject(json, clazz);
        assertSame(clazz, obj.getClass());
//
//        String text = JSON.parseObject(JSON.toJSONString(obj), Feature.OrderedField).toJSONString();
//        if ("{\"age\":99,\"desc\":\"[robohorse\",\"name\":\"xx]\"}".equals(text)) {
//            return;
//        }
//
//        assertEquals("{\"age\":99,\"desc\":\"xx\",\"name\":\"robohorse\"}", text);
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/Person.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Person", bytes, 0, bytes.length);
            }
        }
    }
}
