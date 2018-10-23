package com.alibaba.json.bvt.joda;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.joda.time.DateTimeZone;

public class JodaTest_7_DateTimeZone extends TestCase {
    public void test_for_joda_0() throws Exception {

       Model m = new Model();
       m.zone = DateTimeZone.forID("Asia/Shanghai");

       String json = JSON.toJSONString(m);

       assertEquals("{\"zone\":\"Asia/Shanghai\"}", json);

       Model m1 = JSON.parseObject(json, Model.class);
       assertEquals(m.zone, m1.zone);
    }

    public static class Model {
        public DateTimeZone zone;
    }
}
