package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class TestSpecial_4 extends TestCase {

    public void test_special() throws Exception {
        String json = "{\"大小\":123}";
        JSONObject object = JSON.parseObject(json);
        Object obj = JSONPath.eval(object, "$.大小");
        assertEquals(123, obj);
    }

}
