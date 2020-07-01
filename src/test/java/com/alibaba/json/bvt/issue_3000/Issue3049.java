package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue3049 extends TestCase {
    public void test_for_issue() throws Exception {
        String json1 = "{\"date\":\"2019-11-1 21:45:12\"}";
        MyObject myObject1 = JSON.parseObject(json1, MyObject.class);
        String str2 = JSON.toJSONStringWithDateFormat(myObject1, "yyyy-MM-dd HH:mm:ss");
        assertEquals("{\"date\":\"2019-11-01 21:45:12\"}", str2);
    }

    public static class MyObject {
        public java.util.Date date;
    }
}
