package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.sun.org.apache.xpath.internal.operations.Mod;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wenshao on 16/05/2017.
 */
public class Issue1202 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"date\":\"Apr 27, 2017 5:02:17 PM\"}";
        Model model = JSON.parseObject(text, Model.class);
        assertNotNull(model.date);
        assertEquals("{\"date\":\"Apr 27, 2017 5:02:17 PM\"}", JSON.toJSONString(model));
    }

    public static class Model {
        @JSONField(format = "MMM dd, yyyy h:mm:ss aa")
        public java.util.Date date;
    }
}
