package com.alibaba.json.bvt.joda;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.jdk8.LocalDateTest2;
import junit.framework.TestCase;
import org.junit.Assert;

import org.joda.time.LocalDate;
import java.util.Locale;
import java.util.TimeZone;

public class JodaTest_0 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(LocalDate.now());

        String text = JSON.toJSONString(vo);

        VO vo1 = JSON.parseObject(text, VO.class);

        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    public void test_for_issue_1() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2016-05-06T20:24:28.484\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthOfYear());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    public void test_for_issue_2() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"20160506\"}", VO.class);

        Assert.assertEquals(2016, vo.date.getYear());
        Assert.assertEquals(5, vo.date.getMonthOfYear());
        Assert.assertEquals(6, vo.date.getDayOfMonth());
    }

    /**
     * 方法描述: 测试时间戳转换为 日期
     * @author wuqiong  2017/11/21 16:48
     */
    public void test_for_long() throws Exception {
        String text= "{\"date\":1511248447740}";
        VO vo =JSON.parseObject(text, VO.class);
        Assert.assertEquals(2017, vo.date.getYear());
        Assert.assertEquals(11, vo.date.getMonthOfYear());
        Assert.assertEquals(21, vo.date.getDayOfMonth());
    }

    public static class VO {

        private LocalDate date;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

    }
}
