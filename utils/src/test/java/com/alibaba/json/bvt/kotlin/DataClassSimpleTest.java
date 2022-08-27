package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ASMUtils;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 05/08/2017.
 */
public class DataClassSimpleTest extends TestCase {

    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("DataClassSimple");

        String[] names = ASMUtils.lookupParameterNames(clazz.getConstructors()[0]);
        System.out.println(JSON.toJSONString(names));

        String json = "{\"a\":1001,\"b\":1002}";
        Object obj = JSON.parseObject(json, clazz);
        assertEquals("{\"a\":1001,\"b\":1002}", JSON.toJSONString(obj));

    }

    public static class ExtClassLoader extends ClassLoader {
        Map<String, byte[]> resources = new HashMap<String, byte[]>();

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/DataClassSimple.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                resources.put("DataClassSimple.class", bytes);

                super.defineClass("DataClassSimple", bytes, 0, bytes.length);
            }
        }

        public InputStream getResourceAsStream(String name) {
            byte[] bytes = resources.get(name);
            if (bytes != null) {
                return new ByteArrayInputStream(bytes);
            }
            return super.getResourceAsStream(name);
        }
    }

}
