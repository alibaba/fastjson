package com.alibaba.json.bvt.parser.deser.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.parser.JSONReaderScanner;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;

public class DateTest extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{\"date\":\"2012/04-01\"}", ParserConfig.getGlobalInstance(),
                                                         0);
        parser.setDateFormat("yyyy/MM-dd");
        VO vo = parser.parseObject(VO.class);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM-dd", JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals(dateFormat.parse("2012/04-01"), vo.getDate());
        
        parser.close();
    }

    public void test_reader() throws Exception {

        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner("{\"date\":\"2012/04-01\"}", 0));
        parser.setDateFormat("yyyy/MM-dd");
        VO vo = parser.parseObject(VO.class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM-dd", JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals(dateFormat.parse("2012/04-01"), vo.getDate());

        parser.close();
    }

    public static class VO {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}
