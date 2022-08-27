package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * Created by wenshao on 01/04/2017.
 */
public class MaxBufSizeTest2 extends TestCase {
    public void test_max_buf() throws Exception {
        char[] chars = new char[4096];
        Arrays.fill(chars, '0');


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("val", new String(chars));

        Throwable error = null;
        try {
            toJSONString(jsonObject);
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
    }

    public String toJSONString(Object obj) {

        SerializeWriter out = new SerializeWriter();
        out.setMaxBufSize(4096);
        try {
            new JSONSerializer(out).write(obj);
            return out.toString();
        } finally {
            out.close();
        }
    }
}
