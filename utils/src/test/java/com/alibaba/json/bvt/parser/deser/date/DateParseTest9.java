package com.alibaba.json.bvt.parser.deser.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class DateParseTest9 extends TestCase {

    private static Random random = new Random();
    private TimeZone original = TimeZone.getDefault();
    private String[] zoneIds = TimeZone.getAvailableIDs();

    @Override
    public void setUp() {
        int index = random.nextInt(zoneIds.length);
        TimeZone timeZone = TimeZone.getTimeZone(zoneIds[index]);
        TimeZone.setDefault(timeZone);
        JSON.defaultTimeZone = timeZone;
    }

    @Override
    public void tearDown () {
        TimeZone.setDefault(original);
        JSON.defaultTimeZone = original;
    }

    public void test_date() throws Exception {
        String text = "\"/Date(1242357713797+0800)/\"";
        Date date = JSON.parseObject(text, Date.class);
        assertEquals(date.getTime(), 1242357713797L);
        
        assertEquals(JSONToken.LITERAL_INT, CalendarCodec.instance.getFastMatchToken());

        text = "\"/Date(1242357713797+0545)/\"";
        date = JSON.parseObject(text, Date.class);
        assertEquals(date.getTime(), 1242357713797L);
        assertEquals(JSONToken.LITERAL_INT, CalendarCodec.instance.getFastMatchToken());
    }
    
    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"date\":\"/Date(1242357713797A0800)/\"}", VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"date\":\"/Date(1242357713797#0800)/\"}", VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_dates_different_timeZones() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();

        VO vo = new VO();
        vo.date = now;

        String json = JSON.toJSONString(vo);
        VO result = JSON.parseObject(json, VO.class);
        assertEquals(vo.date, result.date);

        // with iso-format
        json = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat);
        result = JSON.parseObject(json, VO.class);
        assertEquals(JSON.toJSONString(vo.date), JSON.toJSONString(result.date));
    }

    public static class VO {
        public Date date;
    }
}
