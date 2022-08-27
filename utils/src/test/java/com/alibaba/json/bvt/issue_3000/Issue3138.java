package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.Map;

public class Issue3138 extends TestCase {
    public void test_0() throws Exception {
        VO vo = JSON.parseObject("{\"value\":{\"@type\":\"aa\"}}", VO.class);
    }

    public static class VO {
        @JSONField(parseFeatures = Feature.DisableSpecialKeyDetect)
        public Map<String, Object> value;
    }
}
