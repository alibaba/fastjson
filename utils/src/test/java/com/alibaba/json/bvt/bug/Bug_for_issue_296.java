package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_296 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{\"downloadSpeed\":631055,\"responseTime\":1.435,\"url\":\"http://m2.music.126.net/xUqntwOHwpJdXsO_H-kHsw==/5817516022676667.mp3?v=50710699\"}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(631055, obj.get("downloadSpeed"));
    }

    public void test_for_issue_space() throws Exception {
        String text = "{\"downloadSpeed\":631055} ";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(631055, obj.get("downloadSpeed"));
    }
    

    public void test_for_issue_127() throws Exception {
        String text = "{\"downloadSpeed\":631055}\u007f";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(631055, obj.get("downloadSpeed"));
    }
}
