package com.alibaba.json.bvt;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class TestExternal extends TestCase {
    public void test_0 () throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class<?> clazz = classLoader.loadClass("external.VO");
        Object obj = clazz.newInstance();
        String text = JSON.toJSONString(obj);
        System.out.println(text);
        JSON.parseObject(text, clazz);
    }
    
    public static class ExtClassLoader extends ClassLoader {
        public ExtClassLoader() throws IOException{
            super(Thread.currentThread().getContextClassLoader());
            
            byte[] bytes;
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("external/VO.clazz");
            bytes = IOUtils.toByteArray(is);
            is.close();
            
            super.defineClass("external.VO", bytes, 0, bytes.length);
        }
    }
}
