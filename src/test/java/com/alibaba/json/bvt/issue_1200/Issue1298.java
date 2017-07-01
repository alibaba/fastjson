package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Created by wenshao on 30/06/2017.
 */
public class Issue1298 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject object = new JSONObject();

        object.put("date", "2017-06-29T08:06:30.000+05:30");

        Date date = object.getObject("date", Date.class);

        assertEquals("\"2017-06-29T10:36:30\"", JSON.toJSONString(date, SerializerFeature.UseISO8601DateFormat));
    }
}
