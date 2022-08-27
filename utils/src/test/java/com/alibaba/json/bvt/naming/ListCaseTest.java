package com.alibaba.json.bvt.naming;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 09/02/2017.
 */
public class ListCaseTest extends TestCase {
    public void test_0() throws Exception {
        String result = "{\"code\":\"SUCCESS\",\"msg\":\"success\",\"SUCCESS\":true,\"obj\":[\"10.55.251.213\"]}";
        T4QueryResult t4TaskApiResult = JSON.parseObject(result, T4QueryResult.class);
        System.out.println(JSON.toJSONString(t4TaskApiResult));

    }

    public static class Model {
        public List<String> values;
    }

    public static class T4QueryResult {

        @JSONField(name = "OBJ")
        private List<String> obj;

        @JSONField(name = "MSG")
        private String       msg;

        @JSONField(name = "CODE")
        private String       code;

        @JSONField(name = "SUCCESS")
        private Boolean      success;

        public List<String> getObj() {
            return obj;
        }

        public void setObj(List<String> obj) {
            this.obj = obj;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

    }

}
