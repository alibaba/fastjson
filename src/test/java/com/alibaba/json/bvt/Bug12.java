package com.alibaba.json.bvt;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Bug12 extends TestCase {

    public void test_0() throws Exception {
        String resource = "2.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        String text = IOUtils.toString(new InputStreamReader(is, "UTF-8"));
        is.close();
        
        Object obj = JSON.parse(text);
        Assert.assertNotNull(obj);
    }
}
