package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 14/03/2017.
 */
public class A1 extends TestCase {

    public void test_a() throws Exception {
        Object obj = JSON.parse("[{\"feature\":\"\\u3A56\\u3A26\"}]");
        String json = JSON.toJSONString(obj, SerializerFeature.BrowserCompatible);
        System.out.println(json);
    }
}
