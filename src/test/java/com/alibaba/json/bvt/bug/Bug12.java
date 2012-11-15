package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

public class Bug12 extends TestCase {

    public void test_0() throws Exception {
        String resource = "2.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        String text = IOUtils.toString(is);
        is.close();
        
        JSON.parse(text);
    }
}
