package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.*;

public class Issue1660 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.values.add(new Date(1513755213202L));

        String json = JSON.toJSONString(model);
        assertEquals("{\"values\":[\"2017-12-20\"]}", json);
    }

    public static class Model {
        @JSONField(format = "yyyy-MM-dd")
        private List<Date> values = new ArrayList<Date>();

        public List<Date> getValues() {
            return values;
        }
    }
}
