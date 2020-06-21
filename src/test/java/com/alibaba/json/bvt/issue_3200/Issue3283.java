package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue3283 extends TestCase {
    public void test_for_issue() throws Exception {
        VO v = new VO();
        v.v0 = 1001L;
        v.v1 = 101;

        String str = JSON.toJSONString(v, SerializerFeature.WriteNonStringValueAsString);

        assertEquals(":{\"v0\":\"1001\",\"v1\":\"101\"}", str);
    }

    public static class VO {
        public Long v0;
        public Integer v1;
    }
}
