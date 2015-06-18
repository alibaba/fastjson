package com.alibaba.json.bvt.serializer.exception;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RuntimeExceptionTest extends TestCase {

    public void test_0() throws Exception {
        RuntimeException ex = new RuntimeException();

        JSONObject json = JSON.parseObject(JSON.toJSONString(ex));
//        Assert.assertEquals(RuntimeException.class.getName(), json.get("@type"));

        String jsonString = JSON.toJSONString(ex);
        Exception ex1 = JSON.parseObject(jsonString, Exception.class);

        Assert.assertEquals(ex.getMessage(), ex1.getMessage());
        Assert.assertEquals(ex.getStackTrace().length, ex1.getStackTrace().length);

        for (int i = 0; i < ex.getStackTrace().length; ++i) {
            Assert.assertEquals(ex.getStackTrace()[i].getClassName(), ex1.getStackTrace()[i].getClassName());
            Assert.assertEquals(ex.getStackTrace()[i].getFileName(), ex1.getStackTrace()[i].getFileName());
            Assert.assertEquals(ex.getStackTrace()[i].getLineNumber(), ex1.getStackTrace()[i].getLineNumber());
            Assert.assertEquals(ex.getStackTrace()[i].getMethodName(), ex1.getStackTrace()[i].getMethodName());
        }

        Assert.assertEquals(ex1.getClass(), ex.getClass());

        //System.out.println(JSON.toJSONString(ex));
        // Assert.assertEquals("\"java.lang.Boolean\"", JSON.toJSONString(ex));
    }
}
