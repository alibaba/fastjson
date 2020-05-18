package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class StackTraceElementTest extends TestCase {
    public void test_stackTrace() throws Exception {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String text = JSON.toJSONString(stackTrace, SerializerFeature.WriteClassName);
        JSONArray array = (JSONArray) JSON.parse(text);
        for (int i = 0; i < array.size(); ++i) {
            StackTraceElement element = (StackTraceElement) array.get(i);
            Assert.assertEquals(stackTrace[i].getFileName(), element.getFileName());
            Assert.assertEquals(stackTrace[i].getLineNumber(), element.getLineNumber());
            Assert.assertEquals(stackTrace[i].getClassName(), element.getClassName());
            Assert.assertEquals(stackTrace[i].getMethodName(), element.getMethodName());
        }
    }
}
