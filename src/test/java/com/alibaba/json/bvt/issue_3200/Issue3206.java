package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.NameFilter;
import junit.framework.TestCase;

public class Issue3206 extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.date = new java.util.Date(1590819204293L);


        assertEquals(JSON.toJSONString(vo), "{\"date\":\"2020-05-30\"}");

        String str = JSON.toJSONString(vo, new NameFilter() {
            public String process(Object object, String name, Object value) {
                return name;
            }
        });
        assertEquals(str, "{\"date\":\"2020-05-30\"}");
    }

    public static class VO {
        @JSONField(format="yyyy-MM-dd")
        public java.util.Date date;
    }
}
