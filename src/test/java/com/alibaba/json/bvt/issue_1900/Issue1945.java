package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName;

public class Issue1945 extends TestCase {
    public void test_0() throws Exception {
        B b = new B();
        b.clazz = new Class[]{String.class};
        b.aInstance = new HashMap();
        b.aInstance.put("test", "test");
        String s = JSON.toJSONString(b, WriteClassName);
        System.out.println(s);
        B a1 = JSON.parseObject(s, B.class);
    }

    static class B implements Serializable {
        public Class[] clazz;
        public Map aInstance;
    }
}
