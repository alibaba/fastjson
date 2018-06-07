package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import static com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible;

public class Issue1764_bean extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.value = 9007199254741992L;

        String str = JSON.toJSONString(model);
        assertEquals("{\"value\":\"9007199254741992\"}", str);
    }

    @JSONType(serialzeFeatures = BrowserCompatible)
    public static class Model {
        public long value;
    }
}
