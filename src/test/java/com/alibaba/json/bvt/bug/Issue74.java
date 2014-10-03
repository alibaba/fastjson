package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


public class Issue74 extends TestCase {
    public void test_for_issue() throws Exception {
        InputStream is = Issue72.class.getClassLoader().getResourceAsStream("issue74.json");
        String text = org.apache.commons.io.IOUtils.toString(is);
        is.close();
        
        JSONArray json = (JSONArray) JSON.parse(text);
        
        Assert.assertNotNull(json.getJSONObject(0).getJSONObject("dataType").getJSONObject("categoryType").getJSONArray("dataTypes").get(0));
        Assert.assertSame(json.getJSONObject(0).getJSONObject("dataType"), json.getJSONObject(0).getJSONObject("dataType").getJSONObject("categoryType").getJSONArray("dataTypes").get(0));
    }
}
