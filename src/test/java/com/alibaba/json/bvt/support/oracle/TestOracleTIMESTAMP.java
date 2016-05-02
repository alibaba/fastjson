package com.alibaba.json.bvt.support.oracle;

import java.sql.Timestamp;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;
import oracle.sql.DATE;

public class TestOracleTIMESTAMP extends TestCase {
    public void test_0 () throws Exception {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        DATE timestamp = new DATE(date);
        
        String text = JSON.toJSONString(timestamp);
        Assert.assertEquals((date.getTime() / 1000) * 1000, Long.parseLong(text));
    }
}
