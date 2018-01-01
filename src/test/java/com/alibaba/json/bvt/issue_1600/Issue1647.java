package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class Issue1647 extends TestCase {
    public void test_for_issue() throws Exception {
        Params params = new Params()
        .setVerificationIds(Arrays.asList(new String[]{"a", "b"}))
        .setWithFields(true);

        String json = JSON.toJSONString(params);
        System.out.println(json);
        params = JSON.parseObject(json, Params.class);
        assertEquals("{\"verification_ids\":[\"a\",\"b\"],\"with_fields\":true}", JSON.toJSONString(params));
    }

    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class Params {

        private boolean withFields;

        private List<String> verificationIds;

        public boolean isWithFields() {
            return withFields;
        }

        public Params setWithFields(boolean withFields) {
            this.withFields = withFields;
            return this;
        }

        public List<String> getVerificationIds() {
            return verificationIds;
        }

        public Params setVerificationIds(List<String> verificationIds) {
            this.verificationIds = verificationIds;
            return this;
        }
    }


}
