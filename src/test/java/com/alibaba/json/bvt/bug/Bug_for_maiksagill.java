package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.WareHouseInfo;

public class Bug_for_maiksagill extends TestCase {

    public void test_for_maiksagill() throws Exception {
        String resource = "json/maiksagill.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        String text = IOUtils.toString(is);
        
        JSON.parseObject(text, WareHouseInfo[].class);
             
    }
}
