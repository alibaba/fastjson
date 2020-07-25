package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import junit.framework.TestCase;

public class Issue3362 extends TestCase {
    public void test_for_issue() {
        test("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        test("yyyyMMddHHmmssSSSZ");
    }

    private void test(String format) {
        Demo demo = new Demo();
        demo.setDate(new java.util.Date());
        JSON.DEFFAULT_DATE_FORMAT = format;
        String jsonString = JSON.toJSONString(demo,
                new SerializeConfig(),
                new SerializeFilter[]{},
                format,
                JSON.DEFAULT_GENERATE_FEATURE);
        Demo parsedDemo = JSON.parseObject(jsonString, Demo.class);
        assertNotNull(parsedDemo.getDate());
    }

    public static class Demo {
        private java.util.Date date;

        public java.util.Date getDate() {
            return date;
        }

        public void setDate(java.util.Date date) {
            this.date = date;
        }
    }
}