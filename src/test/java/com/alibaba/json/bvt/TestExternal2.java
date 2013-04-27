package com.alibaba.json.bvt;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestExternal2 extends TestCase {

    public void test_0() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();

        Class theClass = classLoader.loadClass("com.alibaba.mock.demo.service.MockDemoService");
        Method[] methods = theClass.getMethods();

        //基本类型
        if (void.class.isPrimitive()) {
            System.out.println("void");
        }
        if (boolean.class.isPrimitive()) {
            System.out.println("boolean");
        }

        for (Method method : methods) {
            System.out.println("name: " + method.getName());

            Class[] paraClassArray = method.getParameterTypes();
            for (Class paraClass : paraClassArray) {
                System.out.println("parameters: " + paraClass);
                
                Package pkg = paraClass.getPackage();
                if (pkg == null || !pkg.getName().equals("java.lang")) {
                    Object obj = paraClass.newInstance();
                  //  System.out.println(obj);

                    String kaka = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
                    System.out.println(kaka);
                    System.out.println(kaka);

//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String tt = objectMapper.writeValueAsString(obj);
//                    System.out.println(tt);
                }
            }
            //System.out.println("return: " + method.getReturnType());
            //System.out.println("description: " + method.toGenericString());
            System.out.println();
        }
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException{
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("external/Demo.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("com.alibaba.mock.demo.api.Demo", bytes, 0, bytes.length);
            }
            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("external/MockDemoService.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();
                
                super.defineClass("com.alibaba.mock.demo.service.MockDemoService", bytes, 0, bytes.length);
            }
        }
    }
}
