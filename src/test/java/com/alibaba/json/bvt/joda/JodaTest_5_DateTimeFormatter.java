package com.alibaba.json.bvt.joda;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.joda.time.format.*;

public class JodaTest_5_DateTimeFormatter extends TestCase {
    public void test_for_joda_0() throws Exception {

        String json = "{\"formatter\":\"yyyyMMdd\"}";
        Model m = JSON.parseObject(json, Model.class);

        assertEquals(DateTimeFormat.forPattern("yyyyMMdd"), m.formatter);
    }

    public static class Model {
        public DateTimeFormatter formatter;
    }
}
