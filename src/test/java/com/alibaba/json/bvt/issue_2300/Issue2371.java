package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.List;

public class Issue2371 extends TestCase {
    public void test_for_issue() throws Exception {
        RpcRespObj<List<Resource>> resources = convertResult(Resource.class);
        assertEquals(2, resources.data.get(0).resourceId.intValue());
        assertEquals("own佛恩", resources.data.get(0).resourceName);
    }

    public static <T> RpcRespObj<List<T>> convertResult(Class<T> type) {
        String str = "{\"status\":0,\"data\":[{\"resourceId\":2,\"resourceName\":\"own佛恩\",\"systemCode\":\"ad\"}]}";
        RpcRespObj<List<T>> result = JSON.parseObject(str, new TypeReference<RpcRespObj<List<T>>>(type) {});
        return result;
    }


    public static class RpcRespObj<T> {
        public Integer status;
        public Integer errcode;
        public Integer errno;
        public T data;
    }


    public static class Resource {
        public Integer resourceId;
        public String resourceName;
        public String systemCode;


    }
}
