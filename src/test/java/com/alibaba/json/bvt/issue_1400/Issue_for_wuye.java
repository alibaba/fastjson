package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Date;

public class Issue_for_wuye extends TestCase {
    public void test_for_issue() throws Exception {
        String poistr = "{\"gmtModified\":\"2017-09-07 16:39:19\",\"gmtCreate\":\"2017-09-07 16:39:19\"}";
        TimeBean poiInfo = JSON.parseObject(poistr, TimeBean.class);
    }

    public static class TimeBean {
        private Date time1;
        private Date time2;

        public Date getTime1() {
            return time1;
        }
        public void setTime1(Date time1) {
            this.time1 = time1;
        }
        public Date getTime2() {
            return time2;
        }
        public void setTime2(Date time2) {
            this.time2 = time2;
        }

        private Date gmtModified;
        private Date gmtCreate;

        public Date getGmtCreate() {
            return gmtCreate;
        }
        public void setGmtCreate(Date gmtCreate) {
            this.gmtCreate = gmtCreate;
        }
        public Date getGmtModified() {
            return gmtModified;
        }
        public void setGmtModified(Date gmtModified) {
            this.gmtModified = gmtModified;
        }
    }
}
