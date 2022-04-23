package com.alibaba.fastjson.jsonpath.issue4070;

import com.alibaba.fastjson.JSONPath;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestIssue4070 {
    //CS304 Issue link: https://github.com/alibaba/fastjson/issues/4070
    @Test
    public void positiveTest() throws Throwable{
        Long a =10000000000L;
        BigInteger b=new BigInteger("10000000000");
        JSONPath path=JSONPath.compile("/") ;
        Boolean equals=path.containsValue(a,b);
        System.out.println(equals);
        assertTrue(equals);
    }
    //CS304 Issue link: https://github.com/alibaba/fastjson/issues/4070
    @Test
    public void negativeTest() throws Throwable{
        Byte a =100;
        BigInteger b=new BigInteger("101");
        JSONPath path=JSONPath.compile("/") ;
        Boolean equals=path.containsValue(a,b);
        System.out.println(equals);
        assertFalse(equals);
    }
}
