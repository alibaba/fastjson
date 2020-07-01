package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue2787 extends TestCase {
    public void test_for_issue() throws Exception {
        Model m = new Model();
        String str = JSON.toJSONString(m, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
        assertEquals("{\"value\":[]}", str);
    }

    public static class Model {
        public int[] value;
    }
}
