package com.alibaba.json.bvt.joda;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import org.joda.time.LocalTime;
import java.util.Locale;
import java.util.TimeZone;

public class JodaTest_3_LocalTimeTest extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setDate(LocalTime.now());
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
        
        VO vo1 = JSON.parseObject(text, VO.class);
        
        Assert.assertEquals(vo.getDate(), vo1.getDate());
    }

    /**
     * 方法描述: 测试时间戳转换为 时间
     * @author wuqiong  2017/11/21 16:48
     */
    public void test_for_long() throws Exception {
        String text= "{\"date\":1511248447740}";
        VO vo =JSON.parseObject(text,VO.class);
        Assert.assertEquals(15, vo.date.getHourOfDay());
        Assert.assertEquals(14, vo.date.getMinuteOfHour());
        Assert.assertEquals(07, vo.date.getSecondOfMinute());
    }

    public static class VO {

        private LocalTime date;

        public LocalTime getDate() {
            return date;
        }

        public void setDate(LocalTime date) {
            this.date = date;
        }

    }
}
