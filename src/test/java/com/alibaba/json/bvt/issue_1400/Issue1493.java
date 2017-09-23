package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Issue1493 extends TestCase {
    public void test_for_issue() throws Exception {
        TestBean test = new TestBean();

        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.parse("2017-09-22 15:08:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        test.setTime1(time1);
        test.setTime2(time2);

        String json = JSON.toJSONStringWithDateFormat(test, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        System.out.println(json);
    }

    public static class TestBean {
        private LocalDateTime time1;
        private LocalDateTime time2;

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
