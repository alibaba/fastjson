package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by wenshao on 16/8/24.
 */
public class Issue784 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "[\"150\",\"change\",{\"timeStamp\":1471595047319,\"value\":\"\"},{\"attrs\":{\"value\":\"\"}}]";
        JSONArray jsonArray = (JSONArray) JSON.parse(json);
        Assert.assertEquals("150", jsonArray.get(0));
        Assert.assertEquals("change", jsonArray.get(1));
        Assert.assertEquals(1471595047319L, jsonArray.getJSONObject(2).get("timeStamp"));
        Assert.assertEquals("", jsonArray.getJSONObject(2).get("value"));
        Assert.assertEquals("", jsonArray.getJSONObject(3).getJSONObject("attrs").get("value"));
        Assert.assertEquals(4, jsonArray.size());
    }
}
