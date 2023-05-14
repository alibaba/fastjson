package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_set_test7 extends TestCase {
    public void test_jsonpath_1() throws Exception {
        JSONObject aa= new JSONObject();
        aa.put("val", "false");
        JSONPath path = JSONPath.compile("$.val");

        path.set(aa, true);
        assertEquals(true, aa.getBoolean("val").booleanValue());

        path.set(aa, false);
        assertEquals(false, aa.getBoolean("val").booleanValue());
    }

    public void test_jsonpath_2() throws Exception {
        VO aa = new VO();
        JSONPath path = JSONPath.compile("$.val");

        path.set(aa, true);
        assertEquals(true, aa.val);

        path.set(aa, false);
        assertEquals(false, aa.val);

        path.set(aa, "true");
        assertEquals(true, aa.val);

        path.set(aa, "false");
        assertEquals(false, aa.val);
    }


    public static class VO {
        public boolean val;
    }

}
