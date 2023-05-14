package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Date;

public class Issue2772 extends TestCase {
    public void test_for_issue() throws Exception {
        {
            java.sql.Time time = java.sql.Time.valueOf("12:13:14");
            long millis = time.getTime();
            assertEquals(Long.toString(millis/1000), JSON.toJSONStringWithDateFormat(time, "unixtime"));
            assertEquals(Long.toString(millis), JSON.toJSONStringWithDateFormat(time, "millis"));
        }

        long millis = System.currentTimeMillis();
        assertEquals(Long.toString(millis), JSON.toJSONStringWithDateFormat(new Date(millis), "millis"));
        assertEquals(Long.toString(millis/1000), JSON.toJSONStringWithDateFormat(new Date(millis), "unixtime"));
    }
}
