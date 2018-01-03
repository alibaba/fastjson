package com.alibaba.json.bvt;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestExternal6 extends TestCase {
    ParserConfig confg = ParserConfig.global;

    protected void setUp() throws Exception {
        confg.addAccept("org.mule.esb.model");
    }

    public void test_0() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class<?> clazz = classLoader.loadClass("org.mule.esb.model.tcc.result.EsbResultModel");
        Method[] methods = clazz.getMethods();
        Method method = clazz.getMethod("setReturnValue", new Class[] { Serializable.class });

        Object obj = clazz.newInstance();
//        method.invoke(obj, "AAAA");

        {
            String text = JSON.toJSONString(obj);
            System.out.println(text);
        }

        String text = JSON.toJSONString(obj, SerializerFeature.WriteClassName, SerializerFeature.WriteMapNullValue);
        System.out.println(text);
        JSON.parseObject(text, clazz, confg);
        String clazzName = JSON.parse(text, confg).getClass().getName();
        Assert.assertEquals(clazz.getName(), clazzName);
    }

    public static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException{
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("external/EsbResultModel.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("org.mule.esb.model.tcc.result.EsbResultModel", bytes, 0, bytes.length);
            }
            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("external/EsbListBean.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();
                
                super.defineClass("org.esb.crm.tools.EsbListBean", bytes, 0, bytes.length);
            }
            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("external/EsbHashMapBean.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();
                
                super.defineClass("org.esb.crm.tools.EsbHashMapBean", bytes, 0, bytes.length);
            }
        }
    }
}
