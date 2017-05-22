package com.alibaba.json.bvt.date;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class DateFieldFormatTest extends TestCase {
    
    public void test_format_() throws Exception {
        Date now = new Date();
        Model model = new Model();
        model.serverTime = now;
        model.publishTime = now;
        
        String text = JSON.toJSONString(model);
        System.out.println(text);
        
        Model model2 = JSON.parseObject(text, Model.class);
        
    }

    public static class Model {

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        public Date serverTime;

        @JSONField(format = "yyyy/MM/dd HH:mm:ss")
        public Date publishTime;
    }
}
