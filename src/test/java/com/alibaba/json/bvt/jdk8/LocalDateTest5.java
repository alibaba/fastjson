package com.alibaba.json.bvt.jdk8;

import java.time.LocalDate;
import java.util.Locale;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class LocalDateTest5 extends TestCase {

    private Locale origin;

    protected void setUp() throws Exception {
        origin = Locale.getDefault();
    }

    protected void tearDown() throws Exception {
        Locale.setDefault(origin);
    }

    public void test_for_tw() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016/05/06\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_jp() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016年5月6日\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_kr() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016년5월6일\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_us() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"05/26/2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(26, vo.date.getDayOfMonth());
    }

    public void test_for_eur() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"26/05/2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(26, vo.date.getDayOfMonth());
    }

    public void test_for_us_1() throws Exception {
        Locale.setDefault(Locale.US);
        VO vo = JSON.parseObject("{\"date\":\"05/06/2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(06, vo.date.getDayOfMonth());
    }

    public void test_for_br() throws Exception {
        Locale.setDefault(new Locale("pt", "BR"));
        VO vo = JSON.parseObject("{\"date\":\"06/05/2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_au() throws Exception {
        Locale.setDefault(new Locale("en", "AU"));
        VO vo = JSON.parseObject("{\"date\":\"06/05/2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_de() throws Exception {
        Locale.setDefault(new Locale("pt", "BR"));
        VO vo = JSON.parseObject("{\"date\":\"06.05.2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }
    
    public void test_for_in() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"06-05-2016\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthValue());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public static class VO {

        public LocalDate date;

    }
}
