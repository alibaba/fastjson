package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 05/05/2017.
 */
public class Issue1187 extends TestCase {
    public void test_for_issue() throws Exception {
        String text1 = "{\"d\":\"2017-04-27+08:00\"}";
        JSONObject jsonObject = (JSONObject) JSON.parse(text1, Feature.AllowISO8601DateFormat);;
        System.out.println(jsonObject.get("d").getClass().getClass());//String
    }
}
