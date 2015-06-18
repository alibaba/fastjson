package com.alibaba.json.bvt.parser.deser;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;

public class DateTest extends TestCase {

    public void test() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{\"date\":\"2012/04-01\"}", ParserConfig.getGlobalInstance(),
                                                         0);
        parser.setDateFormat("yyyy/MM-dd");
        VO vo = parser.parseObject(VO.class);
        
        Assert.assertEquals(new SimpleDateFormat("yyyy/MM-dd").parse("2012/04-01"), vo.getDate());
        
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
