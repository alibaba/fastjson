package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvtVO.BigClass;

import junit.framework.TestCase;

public class FastJsonBigClassTest extends TestCase {

    public void test_big_class() {
        BigClass bigObj = new BigClass();
        String json = JSON.toJSONString(bigObj, SerializerFeature.IgnoreNonFieldGetter);
//        assertThat(json, not(containsString("skipme")));
    }

}
