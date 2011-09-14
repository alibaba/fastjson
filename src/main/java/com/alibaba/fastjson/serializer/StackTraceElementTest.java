package com.alibaba.fastjson.serializer;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class StackTraceElementTest extends TestCase {
    public void test_stackTrace() throws Exception {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String text = JSON.toJSONString(stackTrace, SerializerFeature.WriteClassName);
        System.out.println(text);
        StackTraceElement[]  stackTrace2 = (StackTraceElement[]) JSON.parse(text);
    }
}
