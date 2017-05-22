package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by wenshao on 19/12/2016.
 */
public class Issue942 extends TestCase {
    public void test_for_issue() throws Exception {
        final String pattern = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter   = DateTimeFormatter.ofPattern(pattern);

        String text = JSON.toJSONStringWithDateFormat(dateTime, pattern);
        assertEquals(JSON.toJSONString(formatter.format(dateTime)), text);
    }
}
