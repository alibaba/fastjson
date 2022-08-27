package com.alibaba.json.bvt.bug.bug2019;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Bug20190729_01 extends TestCase
{
    public void test_for_issue() throws Exception {
        JSONObject object = new JSONObject();
        object.put("bucketId", 123);

        JSON.toJavaObject(object, BucketInfo.class);
    }

    public static class BucketInfo {
        private Integer bucketId;

        public Integer getBucketId() {
            return bucketId;
        }

        public void setBucketId(int bucketId) {
            this.bucketId = bucketId;
        }
    }
}

