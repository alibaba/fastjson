package com.alibaba.json.bvt.android;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class AliUserTest extends TestCase {
    private String text;
    
    public void test_for_ali_user() throws Exception {
        InputStream is = AliUserTest.class.getClassLoader().getResourceAsStream("json/aliuser.json");
        text = IOUtils.toString(is);
        
        JSON.parse(text);
    }
    
  
}
