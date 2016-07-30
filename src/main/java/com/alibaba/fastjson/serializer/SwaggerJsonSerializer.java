package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Swagger的Json处理，解决/v2/api-docs获取不到内容导致获取不到API页面内容的问题
 *
 * @Author zhaiyongchao
 * @Blog http://blog.didispace.com
 * @Date 2016/07/04
 */
public class SwaggerJsonSerializer implements ObjectSerializer {

    public final static SwaggerJsonSerializer instance = new SwaggerJsonSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.getWriter();
        try {
            Class clazz = Class.forName("springfox.documentation.spring.web.json.Json");
            Method method = clazz.getDeclaredMethod("value");
            out.write(method.invoke(object).toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
