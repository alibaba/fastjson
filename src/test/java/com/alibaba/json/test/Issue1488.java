package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Issue1488 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        final Class clazz = classLoader.loadClass("Issue1488_Server");

        final int THREAD_NUMBER = 10;
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(THREAD_NUMBER);
        for (int i = 0; i < 10; ++i) {
            System.out.println("start....");
            threadPool.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    Map map = new HashMap();
                    map.put("run_id", "aeca30e");
                    map.put("port", 1002);
                    map.put("processId", 3001);
                    System.out.println(JSON.toJSONString(JSON.parseObject(JSON.toJSONString(map), clazz)));
                }
            }, 1, 1, TimeUnit.SECONDS);
        }

        Thread.sleep(1000 * 1000);

//
//        Object obj = JSON.parseObject(json, clazz);
//        assertEquals("{\"process_id\":301,\"run_id\":\"aeca30e\"}", JSON.toJSONString(obj));
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/Issue1488_Server.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("Issue1488_Server", bytes, 0, bytes.length);
            }
        }
    }
}
