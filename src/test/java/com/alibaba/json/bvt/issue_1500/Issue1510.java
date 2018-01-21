package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Issue1510 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        Model model = JSON.parseObject("{\"startTime\":\"2017-11-04\",\"endTime\":\"2017-11-14\"}", Model.class);
        String text = JSON.toJSONString(model);
        assertEquals("{\"endTime\":\"2017-11-14\",\"startTime\":\"2017-11-04\"}", text);
    }

    public static class Model {
        @JSONField(format = "yyyy-MM-dd")
        private Date startTime;

        @JSONField(format = "yyyy-MM-dd")
        private Date endTime;

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
    }
}
