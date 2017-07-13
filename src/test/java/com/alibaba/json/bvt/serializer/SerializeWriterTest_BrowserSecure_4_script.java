package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

public class SerializeWriterTest_BrowserSecure_4_script extends TestCase {

    public void test_0() throws Exception {
        JSONObject object = new JSONObject();
        object.put("value", "<script>alert(1);</script>");
        String text = JSON.toJSONString(object, SerializerFeature.BrowserSecure);
        assertEquals("{\"value\":\"&lt;script&gt;alert\\u00281\\u0029\\u003B&lt;\\u002Fscript&gt;\"}", text);
    }

}
