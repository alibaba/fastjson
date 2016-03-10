package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvtVO.PayDO;

import junit.framework.TestCase;

public class IgnoreGetMethodTest extends TestCase {
//    public void test_nested_object() {
//        QueryResult result = new QueryResult();
//        result.setPay(new PayDO());
//        String json = JSON.toJSONString(result, SerializerFeature.IgnoreNonFieldGetter);
//        System.out.println(json);
//    }

    public void test() {
        PayDO result = new PayDO();
        String json = JSON.toJSONString(result, SerializerFeature.IgnoreNonFieldGetter);
        System.out.println(json);
    }
}
