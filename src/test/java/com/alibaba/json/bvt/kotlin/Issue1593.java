package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Issue1593 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("Issue1593_JsonBean");

        String json = "{\n" +
                "          \"signature\": \"8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918\",\n" +
                "          \"list\": [\n" +
                "            {\n" +
                "                \"name\":\"aaa\"\n" +
                "            },{\n" +
                "                \"name\":\"bbb\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }";
        Object bean = JSON.parseObject(json, clazz);
        assertEquals("{\"list\":[{\"name\":\"aaa\"},{\"name\":\"bbb\"}],\"signature\":\"8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918\"}"
                , JSON.toJSONString(bean));

        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.toJavaObject(clazz);
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/issue1593/Issue1593_JsonBean.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Issue1593_JsonBean", bytes, 0, bytes.length);
            }
            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/issue1593/Issue1593_Lists.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Issue1593_Lists", bytes, 0, bytes.length);
            }
        }
    }
}
