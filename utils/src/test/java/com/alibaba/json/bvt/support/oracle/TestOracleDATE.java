package com.alibaba.json.bvt.support.oracle;

import java.sql.Timestamp;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;
import oracle.sql.TIMESTAMP;

public class TestOracleDATE extends TestCase {
    public void test_0 () throws Exception {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        TIMESTAMP timestamp = new TIMESTAMP(date);
        
        String text = JSON.toJSONString(timestamp);
        Assert.assertEquals(date.getTime(), Long.parseLong(text));
    }
}
