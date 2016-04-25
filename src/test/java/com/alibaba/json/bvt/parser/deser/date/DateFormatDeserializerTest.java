package com.alibaba.json.bvt.parser.deser.date;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DateFormatDeserializerTest extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_dateFormat_empty() throws Exception {
        VO vo = JSON.parseObject("{\"format\":\"\"}", VO.class);
        Assert.assertEquals(null, vo.getFormat());
    }
    
    public void test_dateFormat_array() throws Exception {
        List<SimpleDateFormat> list = JSON.parseArray("[\"\",null,\"yyyy\"]", SimpleDateFormat.class);
        Assert.assertEquals(null, list.get(0));
        Assert.assertEquals(null, list.get(1));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals(dateFormat, list.get(2));
    }

    public void test_dateFormat_null() throws Exception {
        VO vo = JSON.parseObject("{\"format\":null}", VO.class);
        Assert.assertEquals(null, vo.getFormat());
    }

    public void test_dateFormat_yyyy() throws Exception {
        VO vo = JSON.parseObject("{\"format\":\"yyyy\"}", VO.class);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", JSON.defaultLocale);
        dateFormat.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals(dateFormat, vo.getFormat());
    }

    public void test_dateFormat_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"format\":123}", VO.class);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private SimpleDateFormat format;

        public VO(){

        }

        public VO(SimpleDateFormat format){
            this.format = format;
        }

        public SimpleDateFormat getFormat() {
            return format;
        }

        public void setFormat(SimpleDateFormat format) {
            this.format = format;
        }

    }
}
