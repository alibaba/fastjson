package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.issue_1500.Issue1500;
import junit.framework.TestCase;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Issue1493 extends TestCase {

    public void test_for_issue() throws Exception {

        TestBean test = new TestBean();
        String stime2 = "2017-09-22 15:08:56";

        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.parse(stime2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        test.setTime1(time1);
        test.setTime2(time2);
        String t1 = time1.toString();

        String json = JSON.toJSONString(test, SerializerFeature.WriteDateUseDateFormat);
        Assert.assertEquals("{\"time1\":\""+t1+"\",\"time2\":\""+stime2+"\"}",json);


        JSON.DEFFAULT_LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        String stime1 = DateTimeFormatter.ofPattern(JSON.DEFFAULT_LOCAL_DATE_TIME_FORMAT).format(time1);

        json = JSON.toJSONString(test, SerializerFeature.WriteDateUseDateFormat);
        Assert.assertEquals("{\"time1\":\""+stime1+"\",\"time2\":\""+stime2+"\"}",json);


        json = JSON.toJSONStringWithDateFormat(test, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        Assert.assertEquals("{\"time1\":\""+stime1+"\",\"time2\":\""+stime2+"\"}",json);

    }

    public static class TestBean {
        LocalDateTime time1;
        LocalDateTime time2;

        public LocalDateTime getTime1() {
            return time1;
        }

        public void setTime1(LocalDateTime time1) {
            this.time1 = time1;
        }

        public LocalDateTime getTime2() {
            return time2;
        }

        public void setTime2(LocalDateTime time2) {
            this.time2 = time2;
        }
    }
}
