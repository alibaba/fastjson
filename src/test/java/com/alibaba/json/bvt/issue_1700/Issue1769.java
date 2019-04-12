package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Issue1769 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }

    public void test_for_issue() throws Exception {
        byte[] newby = "{\"beginTime\":\"420180319160440\"}".getBytes();
        QueryTaskResultReq rsp3 = JSON.parseObject(newby, QueryTaskResultReq.class);
        assertEquals("{\"beginTime\":\"152841225111920\"}", new String(JSON.toJSONBytes(rsp3)));
    }

    @JSONType(orders = {"beginTime"})
    public static class QueryTaskResultReq
    {
        private Date beginTime;

        @JSONField(format = "yyyyMMddHHmmss")
        public Date getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Date beginTime) {
            this.beginTime = beginTime;
        }
    }
}
