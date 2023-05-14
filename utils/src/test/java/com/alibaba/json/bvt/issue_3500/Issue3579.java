package com.alibaba.json.bvt.issue_3500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.math.BigDecimal;

public class Issue3579 extends TestCase {
    public void test_for_issue() throws Exception {
        assertEquals("1",
                JSON.toJSONString(new BigDecimal("1"))
        );

        assertEquals("1.",
                JSON.toJSONString(new BigDecimal("1"), SerializerFeature.WriteClassName)
        );
    }
}
