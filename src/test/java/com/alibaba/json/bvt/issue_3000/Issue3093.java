package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.Calendar;

public class Issue3093 extends TestCase {
    public void test_for_issue() throws Exception {
        Timestamp ts = new Timestamp(Calendar.getInstance().getTimeInMillis());
        System.out.println(ts.toString());
        String json = JSON.toJSONString(ts, SerializerFeature.UseISO8601DateFormat);
        System.out.println(json);
    }
}
