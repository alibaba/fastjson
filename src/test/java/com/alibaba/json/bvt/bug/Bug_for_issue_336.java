package com.alibaba.json.bvt.bug;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_336 extends TestCase {

    public void test_for_issue() throws Exception {
        RemoteInvocation remoteInvocation = new RemoteInvocation();
        remoteInvocation.setMethodName("test");
        remoteInvocation.setParameterTypes(new Class[] { int.class, Date.class, String.class });
        remoteInvocation.setArguments(new Object[] { 1, new Date(1460538273131L), "this is a test" });
        String json = JSON.toJSONString(remoteInvocation);
        
        Assert.assertEquals("{\"arguments\":[1,1460538273131,\"this is a test\"],\"methodName\":\"test\",\"parameterTypes\":[\"int\",\"java.util.Date\",\"java.lang.String\"]}", json);
        
        remoteInvocation = JSON.parseObject(json, RemoteInvocation.class);
        
        Assert.assertEquals(3, remoteInvocation.parameterTypes.length);
        Assert.assertEquals(int.class, remoteInvocation.parameterTypes[0]);
        Assert.assertEquals(Date.class, remoteInvocation.parameterTypes[1]);
        Assert.assertEquals(String.class, remoteInvocation.parameterTypes[2]);
        

    }

    public static class RemoteInvocation {

        private String     methodName;
        private Class<?>[] parameterTypes;
        private Object[]   arguments;

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public void setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public void setArguments(Object[] arguments) {
            this.arguments = arguments;
        }

    }
}
