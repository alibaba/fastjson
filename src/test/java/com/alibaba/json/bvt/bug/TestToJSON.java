package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yinx2003
 * @since 2021-08-31 13:32:54
 */
public class TestToJSON {
    @Test
    public void testToJSON() {
        JSONObject obj = new JSONObject();
        JSONObject copyObj = (JSONObject) JSON.toJSON(obj);
        Assert.assertFalse(obj == copyObj);
    }
}
