package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import java.io.Serializable;

public class Issue1558 extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);
        String text = "{\"id\": \"439a3213-e734-4bf3-9870-2c471f43d651\", \"instance\": \"v1\", \"interface\": \"com.xxx.aplan.UICommands\", \"method\": \"start\", \"params\": [\"tony\"], \"@type\": \"com.alibaba.json.bvt.issue_1500.Issue1558$Request\"}";

        JSON.parseObject(text, Request.class, config);
    }

    @JSONType
    public static class Request implements Serializable {
        private String id;
        private String instance;
        private String _interface;
        private String method;
        private Object[] params;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getInstance() {
            return instance;
        }
        public void setInstance(String instance) {
            this.instance = instance;
        }
        public String getInterface() {
            return _interface;
        }
        public void setInterface(String _interface) {
            this._interface = _interface;
        }
        public String getMethod() {
            return method;
        }
        public void setMethod(String method) {
            this.method = method;
        }
        public Object[] getParams() {
            return params;
        }
        public void setParams(Object[] params) {
            this.params = params;
        }
    }
}
