package com.alibaba.json.bvt.date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wenshao on 07/04/2017.
 */
public class DateFieldTest11_reader extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_cn() throws Exception {
        Model vo = new JSONReader(new StringReader("{\"date0\":\"2016-05-06\",\"date1\":\"2017-03-01\"}")).readObject(Model.class);

        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);

        calendar.setTime(vo.date0);
        assertEquals(2016, calendar.get(Calendar.YEAR));
        assertEquals(4, calendar.get(Calendar.MONTH));
        assertEquals(6, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));

        calendar.setTime(vo.date1);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(2, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    public void test_cn_1() throws Exception {
        Model vo = new JSONReader(new StringReader("{\"date0\":1462464000000,\"date1\":1488297600000}")).readObject(Model.class);

        Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);

        calendar.setTime(vo.date0);
        assertEquals(2016, calendar.get(Calendar.YEAR));
        assertEquals(4, calendar.get(Calendar.MONTH));
        assertEquals(6, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));

        calendar.setTime(vo.date1);
        assertEquals(2017, calendar.get(Calendar.YEAR));
        assertEquals(2, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));

        System.out.println(vo.date0.getTime());
        System.out.println(vo.date1.getTime());
    }

    public static class Model {
        public Date date0;
        public Date date1;
    }
}
