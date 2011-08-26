package com.alibaba.json.test.bvt;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class TestTimeUnit extends TestCase {
    public void test_0 () throws Exception {
        String text = JSON.toJSONString(TimeUnit.DAYS);
        Assert.assertEquals(TimeUnit.DAYS, JSON.parseObject(text, TimeUnit.class));
    }
}
