package com.alibaba.json.bvt;

import java.io.Serializable;
import java.util.Date;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Issue213Test extends TestCase {

    public void test_0() throws Exception {
        String text = "\t\t\t\t\t\t \u00A020:00-21:30</span><br />\r\n\r\n</p>\r\n<p>\r\n\t\r\n</p>\r\n<p>\r\n\t<br />\r\n</p>\r\n\t\t\t";
        Product e = new Product();
        e.setIntro(text);
        byte[] r = JSON.toJSONBytes(e);
        JSON.parseObject(r, Product.class);
    }

    public static class Product implements Serializable {

        private static final long serialVersionUID = 5515785177596600948L;

        private String            studyTargets;

        private String            applicableUsers;

        private String            intro;

        private Date              createDateTime;

        private int               createUserId;

        private int               liveStatus;

        public String getStudyTargets() {
            return studyTargets;
        }

        public void setStudyTargets(String studyTargets) {
            this.studyTargets = studyTargets;
        }

        public String getApplicableUsers() {
            return applicableUsers;
        }

        public void setApplicableUsers(String applicableUsers) {
            this.applicableUsers = applicableUsers;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(int createUserId) {
            this.createUserId = createUserId;
        }

        public int getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(int liveStatus) {
            this.liveStatus = liveStatus;
        }

        public Date getCreateDateTime() {
            return createDateTime;
        }

        public void setCreateDateTime(Date createDateTime) {
            this.createDateTime = createDateTime;
        }
    }
}
