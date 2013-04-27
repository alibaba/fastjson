package com.alibaba.json.bvt;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class DateTest extends TestCase {
    public void test_date() throws Exception {
        long millis = 1324138987429L;
        Date date = new Date(millis);
        
        System.out.println(JSON.toJSONString(date));
        
        System.out.println(JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat));
        System.out.println(JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
