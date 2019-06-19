package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import static com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible;

public class Issue1764_bean extends TestCase {
    public void test_for_issue() throws Exception {
        assertEquals("{\"value\":\"9007199254741992\"}"
                , JSON.toJSONString(
                        new Model(9007199254741992L)));

        assertEquals("{\"value\":\"9007199254741990\"}"
                , JSON.toJSONString(
                        new Model(9007199254741990L)));

        assertEquals("{\"value\":100}"
                , JSON.toJSONString(
                        new Model(100L)));

        assertEquals("{\"value\":\"-9007199254741990\"}"
                , JSON.toJSONString(
                        new Model(-9007199254741990L)));

        assertEquals("{\"value\":-9007199254740990}"
                , JSON.toJSONString(
                        new Model(-9007199254740990L)));

    }



    @JSONType(serialzeFeatures = BrowserCompatible)
    public static class Model {
        public long value;

        public Model() {

        }

        public Model(long value) {
            this.value = value;
        }
    }
}
