package com.alibaba.json.bvt.parser.deser.date;

import java.util.Date;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class DateParseTest11 extends TestCase {

    public void test() throws Exception {
        VO vo = JSON.parseObject("{\"date\":\"2012-04-01T12:04:05.555\"}", VO.class);
    }

    public static class VO {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

    }
}
