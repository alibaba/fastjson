package com.alibaba.json.bvt.parser.deser;

import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DateFormatDeserializerTest extends TestCase {

    public void test_dateFormat_empty() throws Exception {
        VO vo = JSON.parseObject("{\"format\":\"\"}", VO.class);
        Assert.assertEquals(null, vo.getFormat());
    }

    public void test_dateFormat_null() throws Exception {
        VO vo = JSON.parseObject("{\"format\":null}", VO.class);
        Assert.assertEquals(null, vo.getFormat());
    }

    public void test_dateFormat_yyyy() throws Exception {
        VO vo = JSON.parseObject("{\"format\":\"yyyy\"}", VO.class);
        Assert.assertEquals(new SimpleDateFormat("yyyy"), vo.getFormat());
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
