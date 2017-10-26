package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Issue1543 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazzUser = classLoader.loadClass("User1");
        Class clazzCluster = classLoader.loadClass("Cluster");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("name", "test1");

        JSON.parseObject(JSON.toJSONString(map), clazzUser);
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/issue1543/User1.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("User1", bytes, 0, bytes.length);
            }

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/issue1543/Cluster.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Cluster", bytes, 0, bytes.length);
            }
        }
    }
}
