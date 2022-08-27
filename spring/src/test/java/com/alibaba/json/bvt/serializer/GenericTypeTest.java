package com.alibaba.json.bvt.serializer;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class GenericTypeTest extends TestCase {
    
    public void test_gerneric() throws Exception {
        MyResultResult result = new MyResultResult();
        JSON.toJSONString(result);
    }
    
    public static class MyResultResult extends BaseResult<String> {
    }
    
    public static class BaseResult<T> implements Serializable {
        public T data;
    }
}
