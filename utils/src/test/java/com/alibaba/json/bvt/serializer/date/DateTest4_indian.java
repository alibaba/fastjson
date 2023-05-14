package com.alibaba.json.bvt.serializer.date;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTest4_indian extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_date() throws Exception {
        Date date1 = JSON.parseObject("{\"gmtCreate\":\"2018-09-11T21:29:34+0530\"}", VO.class).getGmtCreate();
        assertNotNull(date1);
        Date date2 = JSON.parseObject("{\"gmtCreate\":\"2018-09-11T21:29:34+0500\"}", VO.class).getGmtCreate();
        Date date3 = JSON.parseObject("{\"gmtCreate\":\"2018-09-11T21:29:34+0545\"}", VO.class).getGmtCreate();
        Date date4 = JSON.parseObject("{\"gmtCreate\":\"2018-09-11T21:29:34+1245\"}", VO.class).getGmtCreate();
        Date date5 = JSON.parseObject("{\"gmtCreate\":\"2018-09-11T21:29:34+1345\"}", VO.class).getGmtCreate();

        long delta_2_1 = date2.getTime() - date1.getTime();
        assertEquals(1800000, delta_2_1);

        long delta_3_1 = date3.getTime() - date1.getTime();
        assertEquals(-900000, delta_3_1);

        long delta_4_3 = date4.getTime() - date3.getTime();
        assertEquals(-25200000, delta_4_3);

        long delta_5_4 = date5.getTime() - date4.getTime();
        assertEquals(-3600000, delta_5_4);

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
