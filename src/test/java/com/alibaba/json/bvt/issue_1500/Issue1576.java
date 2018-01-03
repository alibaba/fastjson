package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class Issue1576 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"code\":200,\"in_msg\":\"a\",\"out_msg\":\"a\",\"data\":[{\"title\":\"a\",\"url\":\"url\",\"content\":\"content\"}],\"client_id\":0,\"client_param\":0,\"userid\":0}";
        NewsDetail newsDetail = JSON.parseObject(json, NewsDetail.class);
        assertNotNull(newsDetail);
    }

    public static class NewsDetail {

        public int code;
        public String in_msg;
        public String out_msg;
        public String client_id;
        public String client_param;
        public String userid;
        public List<DataBean> data = new ArrayList<DataBean>();

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getIn_msg() {
            return in_msg;
        }

        public void setIn_msg(String in_msg) {
            this.in_msg = in_msg;
        }

        public String getOut_msg() {
            return out_msg;
        }

        public void setOut_msg(String out_msg) {
            this.out_msg = out_msg;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getClient_param() {
            return client_param;
        }

        public void setClient_param(String client_param) {
            this.client_param = client_param;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }
    }

    public static class DataBean {
        /**
         * title : 中午
         * url : url
         * content : content
         */

        public String title;
        public String url;
        public String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
