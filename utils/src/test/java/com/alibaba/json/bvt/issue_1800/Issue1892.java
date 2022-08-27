package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.time.LocalDateTime;

public class Issue1892 extends TestCase {
    public void test_for_issue() throws Exception {
        assertEquals("\"2018-10-10T00:00:00\"",
            JSON.toJSONString(
                    LocalDateTime.of(2018, 10, 10, 0, 0)
            )
        );
    }

    public void test_for_issue_1() throws Exception {
        String json = JSON.toJSONString(
                LocalDateTime.of(2018, 10, 10, 0, 0, 40, 788000000)
        );
        assertEquals("\"2018-10-10T00:00:40.788\"", json);
    }
}
