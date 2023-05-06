package com.alibaba.fastjson.jsonpath.issue4385;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

/**
 * @author vdisk
 */
public class TestIssue4385 {

    @Test
    public void testIssue4385() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1", "");
        Object value = JSONPath.eval(jsonObject, "$.key1.key2");
        Assert.assertNull(value);
    }
}
