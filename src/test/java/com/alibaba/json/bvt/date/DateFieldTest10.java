package com.alibaba.json.bvt.date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wenshao on 07/04/2017.
 */
public class DateFieldTest10 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_zero() throws Exception {
        String text = "{\"date\":\"0000-00-00\"}";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Object object = format.parse("0000-00-00");
        JSON.parseObject(text, Model.class);
    }

    public void test_1() throws Exception {
        String text = "{\"date\":\"2017-08-14 19:05:30.000|America/Los_Angeles\"}";
        JSON.parseObject(text, Model.class);
    }

    public void test_2() throws Exception {
        String text = "{\"date\":\"2017-08-16T04:29Z\"}";
        Model model = JSON.parseObject(text, Model.class);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Object object = format.parse("2017-08-16 04:29");
//        assertEquals(object, model.date);
    }

    public void test_3() throws Exception {
        String text = "{\"date\":\"2017-08-16 04:29\"}";
        Model model = JSON.parseObject(text, Model.class);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Object object = format.parse("2017-08-16 04:29");
//        assertEquals(object, model.date);
    }

    public void test_4() throws Exception {
        String text = "{\"date\":\"2017-08-16T04:29\"}";
        Model model = JSON.parseObject(text, Model.class);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Object object = format.parse("2017-08-16 04:29");
//        assertEquals(object, model.date);
    }

    public void test_5() throws Exception {
        String text = "{\"date\":\"2018-05-21T14:39:44.907+08:00\"}";
        Model model = JSON.parseObject(text, Model.class);
        String str = JSON.toJSONString(model, SerializerFeature.UseISO8601DateFormat);
        assertEquals("{\"date\":\"2018-05-21T14:39:44.907+08:00\"}", str);

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date object = format.parse("2018-05-21T14:39:44.9077913+08:00");
//        assertEquals(object.getTime(), model.date.getTime());
    }

    public void test_6() throws Exception {
        String text = "{\"date\":\"4567-08-16T04:29\"}";
        Model model = JSON.parseObject(text, Model.class);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Object object = format.parse("2017-08-16 04:29");
//        assertEquals(object, model.date);
    }

    public static class Model {
        public Date date;
    }
}
