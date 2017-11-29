package com.alibaba.json.bvt.jdk8;

import java.time.LocalDate;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class LocalDateTest extends TestCase {
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

    /**
     * 方法描述: 测试时间戳转换为 日期
     * @author wuqiong  2017/11/21 16:48
     */
    public void test_for_long() throws Exception {
        String text= "{\"date\":1511248447740}";
        VO vo =JSON.parseObject(text,VO.class);
        Assert.assertEquals(2017, vo.date.getYear());
        Assert.assertEquals(11, vo.date.getMonthValue());
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
