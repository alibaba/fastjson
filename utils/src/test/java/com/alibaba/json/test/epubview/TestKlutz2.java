package com.alibaba.json.test.epubview;

import java.util.HashMap;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestKlutz2 extends TestCase {
//    public void test_0 () throws Exception {
//        EpubViewMetaData x = new EpubViewMetaData();
//        x.setProperties(new HashMap<String, String>());
//        
//        String str = JSON.toJSONString(x);
//        System.out.println(str);
//        
//        JSON.parseObject(str, EpubViewMetaData.class);
//    }
    
    public void test_page () throws Exception {
        EpubViewPage x = new EpubViewPage();
        x.setImageUrl("xxx");
        
        String str = JSON.toJSONString(x);
        System.out.println(str);
        
        JSON.parseObject(str, EpubViewPage.class);
    }
}
