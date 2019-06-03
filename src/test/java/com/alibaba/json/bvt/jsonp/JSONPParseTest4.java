package com.alibaba.json.bvt.jsonp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 21/02/2017.
 */
public class JSONPParseTest4 extends TestCase {
    public void test_f() throws Exception {
        JSONPObject p = new JSONPObject();
        p.setFunction("f");
        assertEquals("f()", p.toJSONString());
    }
}
