package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue3279 extends TestCase {
    public void test_for_issue() throws Exception {
        V0 v = JSON.parseObject("{\"id\":\" 1001 \"}", V0.class, Feature.TrimStringFieldValue);
        assertEquals("1001", v.id);

        v = JSON.parseObject("{\"id\":\" 1001 \"}", V0.class);
        assertEquals(" 1001 ", v.id);
    }

    public void test_for_issue_1() throws Exception {
        V1 v = JSON.parseObject("{\"id\":\" 1001 \"}", V1.class);
        assertEquals("1001", v.id);
    }

    public void test_for_issue_2() throws Exception {
        V2 v = JSON.parseObject("{\"id\":\" 1001 \"}", V2.class);
        assertEquals("1001", v.id);
    }

    public static class V0 {
        public String id;
    }

    public static class V1 {
        @JSONField(parseFeatures = Feature.TrimStringFieldValue)
        public String id;
    }

    @JSONType(parseFeatures = Feature.TrimStringFieldValue)
    public static class V2 {
        public String id;
    }
}
