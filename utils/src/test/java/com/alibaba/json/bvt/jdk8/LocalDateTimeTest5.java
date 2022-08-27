package com.alibaba.json.bvt.jdk8;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class LocalDateTimeTest5 extends TestCase {

    private static Random random = new Random();

    private Locale origin;
    private TimeZone original = TimeZone.getDefault();
    private String[] zoneIds = TimeZone.getAvailableIDs();

    @Override
    protected void setUp() throws Exception {
        int index = random.nextInt(zoneIds.length);
        TimeZone timeZone = TimeZone.getTimeZone(zoneIds[index]);
        TimeZone.setDefault(timeZone);
        JSON.defaultTimeZone = timeZone; // While running mvn tests defaultTimeZone might already be initialized
        origin = Locale.getDefault();
    }

    @Override
    protected void tearDown() throws Exception {
        TimeZone.setDefault(original);
        JSON.defaultTimeZone = original;
        Locale.setDefault(origin);
    }

    public void test_for_long() throws Exception {
        long millis = 1322874196000L;
        // using localDataTime instance so that different timeZones are tested
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(millis), TimeZone.getDefault().toZoneId());
        VO vo = JSON.parseObject("{\"date\":" + millis + "}", VO.class);

        assertEquals("Not Matching year", localDateTime.getYear(), vo.date.getYear());
        assertEquals("Not Matching month", localDateTime.getMonthValue(), vo.date.getMonthValue());
        assertEquals("Not Matching day", localDateTime.getDayOfMonth(), vo.date.getDayOfMonth());
        assertEquals("Not Matching hour", localDateTime.getHour(), vo.date.getHour());
        assertEquals("Not Matching minute", localDateTime.getMinute(), vo.date.getMinute());
        assertEquals("Not Matching second", localDateTime.getSecond(), vo.date.getSecond());
        assertEquals("Not Matching nano", localDateTime.getNano(), vo.date.getNano());
    }
    
    public void test_for_normal() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2011-12-03 09:03:16\"}", VO.class);
        
        assertEquals(2011, vo.date.getYear());
        assertEquals(12, vo.date.getMonthValue());
        assertEquals(3, vo.date.getDayOfMonth());
        assertEquals(9, vo.date.getHour());
        assertEquals(3, vo.date.getMinute());
        assertEquals(16, vo.date.getSecond());
        assertEquals(0, vo.date.getNano());
    }
    
    public void test_for_iso() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2011-12-03T09:03:16\"}", VO.class);
        
        assertEquals(2011, vo.date.getYear());
        assertEquals(12, vo.date.getMonthValue());
        assertEquals(3, vo.date.getDayOfMonth());
        
        assertEquals(9, vo.date.getHour());
        assertEquals(3, vo.date.getMinute());
        assertEquals(16, vo.date.getSecond());
        assertEquals(0, vo.date.getNano());
    }
    
    public void test_for_tw() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016/05/06 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_jp() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016年5月6日 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
    }
    
    public void test_for_cn() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016年5月6日 9时3分16秒\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_kr() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016년5월6일 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_us() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"05/26/2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(26, vo.date.getDayOfMonth());
    }

    public void test_for_eur() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"26/05/2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(26, vo.date.getDayOfMonth());
    }

    public void test_for_us_1() throws Exception {
        Locale.setDefault(Locale.US);
        VO vo = JSON.parseObject("{\"date\":\"05/06/2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(06, vo.date.getDayOfMonth());
    }

    public void test_for_br() throws Exception {
        Locale.setDefault(new Locale("pt", "BR"));
        VO vo = JSON.parseObject("{\"date\":\"06/05/2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_au() throws Exception {
        Locale.setDefault(new Locale("en", "AU"));
        VO vo = JSON.parseObject("{\"date\":\"06/05/2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_de() throws Exception {
        Locale.setDefault(new Locale("pt", "BR"));
        VO vo = JSON.parseObject("{\"date\":\"06.05.2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
        
        assertEquals(9, vo.date.getHour());
        assertEquals(3, vo.date.getMinute());
        assertEquals(16, vo.date.getSecond());
        assertEquals(0, vo.date.getNano());
    }
    
    public void test_for_in() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"06-05-2016 09:03:16\"}", VO.class);

        assertEquals(2016, vo.date.getYear());
        assertEquals(5, vo.date.getMonthValue());
        assertEquals(6, vo.date.getDayOfMonth());
        
        assertEquals(9, vo.date.getHour());
        assertEquals(3, vo.date.getMinute());
        assertEquals(16, vo.date.getSecond());
        assertEquals(0, vo.date.getNano());
    }

    public static class VO {
        public LocalDateTime date;
    }
}
