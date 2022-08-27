package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Zoujing extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("com.alidme.xrecharge.platform.common.data.NoticeData");

        String json = "{\"benefitNoticeState\":1}";
        Object obj = JSON.parseObject(json, clazz);
        String result = JSON.toJSONString(obj);
        System.out.println(result);
        assertEquals("{\"benefitNoticeState\":1,\"outId\":\"\"}", result);
    }

    private static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/zuojing/NoticeData.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("com.alidme.xrecharge.platform.common.data.NoticeData", bytes, 0, bytes.length);
            }

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/zuojing/NoticeDataKt.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("com.alidme.xrecharge.platform.common.data.NoticeDataKt", bytes, 0, bytes.length);
            }

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/zuojing/NoticeData_Companion.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("com.alidme.xrecharge.platform.common.data.NoticeData$Companion", bytes, 0, bytes.length);
            }
        }
    }
}
