package com.alibaba.json.test.epubview;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestKlutz3 extends TestCase {
    public void test_0 () throws Exception {
        EpubViewBook book = new EpubViewBook();
        book.setBookName("xx");
        
        book.setPageList(new ArrayList<EpubViewPage>());
        
        EpubViewPage page = new EpubViewPage();
        book.getPageList().add(page);
        
        EpubViewMetaData metadata = new EpubViewMetaData();
        metadata.setProperties(new HashMap<String, String>());
        
//        book.setMetadata(null);
        
        String str = JSON.toJSONString(book);
        System.out.println(str);
        
        JSON.parseObject(str, EpubViewBook.class);
    }
}
