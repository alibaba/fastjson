package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wenshao on 16/05/2017.
 */
public class Issue1202 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.US;
    }

    public void test_for_issue() throws Exception {
        String text = "{\"date\":\"Apr 27, 2017 5:02:17 PM\"}";
        Model model = JSON.parseObject(text, Model.class);
        assertNotNull(model.date);
//        assertEquals("{\"date\":\"Apr 27, 2017 5:02:17 PM\"}", JSON.toJSONString(model));
    }

    public static class Model {
        @JSONField(format = "MMM dd, yyyy h:mm:ss aa")
        private java.util.Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
