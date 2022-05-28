package com.alibaba.fastjson.serializer.issue4105;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class Issue4105Test {
        @Test
        public void trialOnAIBError() {
            Date date = new Date(8800519659559150410L);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);
            Assert.assertEquals("{\"date\":\"278879336-03-20T09:59:10.410+08:00\"}",jsonObject.toString(SerializerFeature.UseISO8601DateFormat));
        }

        @Test
        public void testForNormalCase() {
            Date date = new Date(888888);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);
            Assert.assertEquals("{\"date\":\"1970-01-01T08:14:48.888+08:00\"}",jsonObject.toString(SerializerFeature.UseISO8601DateFormat));
        }

}
