package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by wenshao on 19/12/2016.
 */
public class Issue952 extends TestCase {
    public void test_for_issue() throws Exception {
        final String pattern = "yyyy-MM-dd'T'HH:mm:ss";

        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter formatter   = DateTimeFormatter.ofPattern(pattern);

        String text = JSON.toJSONString(dateTime, SerializerFeature.UseISO8601DateFormat);
        assertEquals(JSON.toJSONString(formatter.format(dateTime)), text);
    }
}
