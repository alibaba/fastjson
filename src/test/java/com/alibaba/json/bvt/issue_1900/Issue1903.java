package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.beans.Transient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
public class Issue1903 extends TestCase {
    public void test_issue() throws Exception {
        MapHandler mh = new MapHandler();
        mh.add("name", "test");
        mh.add("age", 20);

        Issues1903 issues = (Issues1903) Proxy.newProxyInstance(mh.getClass().getClassLoader(), new Class[]{Issues1903.class}, mh);
        System.out.println(issues.getName());
        System.out.println(issues.getAge());

        System.out.println(JSON.toJSON(issues).toString()); //正确结果: {"age":20}
        System.out.println(JSON.toJSONString(issues));  //正确结果: {"age":20}
        Assert.assertEquals("{\"age\":20}", JSON.toJSON(issues).toString());
        Assert.assertEquals("{\"age\":20}", JSON.toJSONString(issues));
    }

    interface Issues1903{
        @Transient
        @JSONField(serialzeFeatures = { SerializerFeature.SkipTransientField })
        public String getName();
        public void setName(String name);

        public Integer getAge();
        public void setAge(Integer age);
    }


    class MapHandler implements InvocationHandler {
        Map<String, Object> map = new HashMap<String, Object>();

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName().substring(3);
            String first = String.valueOf(name.charAt(0));
            name = name.replaceFirst(first, first.toLowerCase());
            return map.get(name);
        }

        public void add(String key, Object val){
            map.put(key, val);
        }
    }
}
