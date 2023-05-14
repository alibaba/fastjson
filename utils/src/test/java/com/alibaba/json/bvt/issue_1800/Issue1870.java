package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.List;

public class Issue1870 extends TestCase {
    public void test_for_issue() throws Exception {

    }

    public static class Comment {
        @JSONField(name = "pic_arr")
        public List<Pic> pics;


        public List<Pic> getPics() {
            return pics;
        }

        public void setPics(List<Pic> pics) {
            this.pics = pics;
        }
    }

    public static class Pic {
        public int height;
        public String tburl;
        public String url;
        public String width;
    }
}
