package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_json_array extends TestCase {

    public void test_bug() throws Exception {
        String jsonStr = "{\"state\":0,\"data\":[{\"items\":[{\"tip\":\"\u5218\u82e5\u82f1\",\"url\":\"xiami:\\/\\/artist\\/1930\"},{\"tip\":\"\u5218\u5fb7\u534e\",\"url\":\"xiami:\\/\\/artist\\/648\"}],\"type\":\"artist\"},{\"items\":[{\"tip\":\"\u6f02\u6d0b\u8fc7\u6d77\u6765\u770b\u4f60 (Live) - \u5218\u660e\u6e58\",\"url\":\"xiami:\\/\\/song\\/1773431302\"},{\"tip\":\"\\u6211\\u4eec\\u6ca1\\u6709\\u5728\\u4e00\\u8d77 - \\u5218\\u82e5\\u82f1\",\"url\":\"xiami:\\/\\/song\\/1769471863\"},{\"tip\":\"\\u54ed\u7802 (Live)(\\u5218\\u660e\\u6e58\\u80dc\\u51fa) - \\u5218\u660e\u6e58\",\"url\":\"xiami:\\/\\/ song\\/1773484887\"}],\"type\":\"song\"},{\"items\":[{\"tip\":\"\\u4eb2\\u7231\\u7684\\u8def\\u4eba - \\u5218\\u82e5\\u82f1\",\"url\":\"xiami:\\/\\/album\\/55230\"},{\"tip\":\"\\u5728\\u4e00\\u8d77 - \\u5218\\u82e5\\u82f1\",\"url\":\"xiami:\\/\\/album\\/377241\"}],\"type\":\"album\"}],\"status\":\"ok\",\"err\":null} ";

        Parser parser = JSON.parseObject(jsonStr, Parser.class);

        System.out.println(JSON.toJSONString(parser));
    }

    public static class Parser {

        public int    state;
        public JSON   data;
        public String status;
        public String err;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public JSON getData() {
            return data;
        }

        public void setData(JSON data) {
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErr() {
            return err;
        }

        public void setErr(String err) {
            this.err = err;
        }
    }
}
