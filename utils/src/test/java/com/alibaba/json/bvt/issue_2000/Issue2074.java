package com.alibaba.json.bvt.issue_2000;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue2074 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject object = new JSONObject();
        object.put("name", null);

        assertEquals("{\"name\":null}"
                , object.toString(SerializerFeature.WriteMapNullValue));
    }
}
