package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Test;

import static org.junit.Assert.fail;

public class Bug89 {
    @Test
    public void testBug89() {
        try {
            String s = "{\"a\":з」∠)_,\"}";
            JSON.parseObject(s);
            fail("Expect JSONException");
        } catch (JSONException e) {
            // good
        }
    }
}
