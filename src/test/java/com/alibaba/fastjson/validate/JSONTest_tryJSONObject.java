package com.alibaba.fastjson.validate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ IDE    ：IntelliJ IDEA.
 * @ Date   ：2020/8/21  17:35
 * @ Desc   ：test  JSON.tryJSONObject
 */
public class JSONTest_tryJSONObject {

    @Test
    public void testTryJSONObject() throws Exception {
        Assert.assertTrue(JSON.tryJSONObject("{}"));
        Assert.assertFalse(JSON.tryJSONObject("{haha}"));

        Assert.assertTrue(JSON.isJSONObject("{}"));
        Assert.assertFalse(JSON.isJSONObject("{haha}"));

        Assert.assertTrue(JSON.tryJSONArray("[]"));
        Assert.assertFalse(JSON.tryJSONArray("[hehe]"));

        Assert.assertTrue(JSON.isJSONArray("[]"));
        Assert.assertFalse(JSON.isJSONArray("[hehe]"));
    }

}
