package com.alibaba.json.bvt.serializer.date;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Date;

public class DateTest4_indian extends TestCase {

    public void test_date() throws Exception {
        String text = "{\"gmtCreate\":\"2018-09-11T21:29:34+0530\"}";
        
        Date date = JSON.parseObject(text, VO.class).getGmtCreate();
        Assert.assertNotNull(date);
    }

    public static class VO {

        private Date gmtCreate;

        public Date getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

    }
}
