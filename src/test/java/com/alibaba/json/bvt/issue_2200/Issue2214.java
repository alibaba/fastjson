package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Issue2214 extends TestCase {
    public void test_for_issue() throws Exception {
        List list = new ArrayList();
        list.add("robin");
        Object[] args = new Object[]{new List[][]{new List[]{list}}};
        String text = JSON.toJSONString(args);
        Class clazz = User.class;
        Method method = clazz.getMethod("testGenericArrayArray2", List[][].class);
        Type[] types = method.getGenericParameterTypes();
        List argList = JSON.parseArray(text, types);
        Object res = new User().testGenericArrayArray2((List[][]) argList.get(0));
        System.out.println(res);
    }

    public static class User {
        public List[][] testGenericArrayArray2(List[][] res){
            return res;
        }
    }
}
