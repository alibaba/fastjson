package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Bug_for_sanxiao extends TestCase {

    public void test_0() throws Exception {

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/Bug_for_sanxiao.json");
        String text = IOUtils.toString(is);
        is.close();

        JSONObject obj = JSON.parseObject(text);
        System.out.println(obj);
        Assert.assertEquals(obj.getJSONArray("segments").getJSONObject(0),
                            obj.getJSONArray("passengerSegmentItems").getJSONObject(0).get("segment"));
        Assert.assertEquals(1428,
                            obj.getJSONArray("passengerSegmentItems").getJSONObject(0).getJSONObject("segment").getIntValue("agentId"));
    }

}
