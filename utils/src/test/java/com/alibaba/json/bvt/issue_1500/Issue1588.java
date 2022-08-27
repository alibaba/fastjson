package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Issue1588 extends TestCase {
    public void test_for_issue() throws Exception {
        String dateString = "2017-11-17 00:00:00";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("test", date);
        System.out.println(jsonObject.toJSONString(jsonObject, SerializerFeature.UseISO8601DateFormat));
        System.out.println(JSONObject.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd'T'HH:mm:ssXXX"));
    }
}
