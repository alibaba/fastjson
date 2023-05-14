package com.alibaba.json.bvt.serializer.date;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTest5_iso8601 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_date() throws Exception {
        Date date1 = JSON.parseObject("{\"gmtCreate\":\"2018-09-12\"}", VO.class).getGmtCreate();
        assertNotNull(date1);
        Date date2 = JSON.parseObject("{\"gmtCreate\":\"2018-09-12T15:10:19+00:00\"}", VO.class).getGmtCreate();
        Date date3 = JSON.parseObject("{\"gmtCreate\":\"2018-09-12T15:10:19Z\"}", VO.class).getGmtCreate();
        Date date4 = JSON.parseObject("{\"gmtCreate\":\"20180912T151019Z\"}", VO.class).getGmtCreate();
        Date date5 = JSON.parseObject("{\"gmtCreate\":\"2018-09-12T15:10:19Z\"}", VO.class).getGmtCreate();
        Date date6 = JSON.parseObject("{\"gmtCreate\":\"20180912\"}", VO.class).getGmtCreate();

        long delta_2_1 = date2.getTime() - date1.getTime();
        assertEquals(83419000, delta_2_1);

        long delta_3_1 = date3.getTime() - date1.getTime();
        assertEquals(83419000, delta_3_1);

        long delta_4_3 = date4.getTime() - date3.getTime();
        assertEquals(0, delta_4_3);

        long delta_5_4 = date5.getTime() - date4.getTime();
        assertEquals(0, delta_5_4);

        long delta_6_1 = date6.getTime() - date1.getTime();
        assertEquals(0, delta_6_1);


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
