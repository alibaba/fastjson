package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

/**
 * Created by wenshao on 23/07/2017.
 */
public class Issue1320 extends TestCase {
    public void test_for_issue() throws Exception {
        SSOToken token = new SSOToken();
        JSON.toJSONString(token);
    }

    @SuppressWarnings("serial")
    public static class SSOToken extends Token {

        /* 登录类型 */
        private Integer type;

        /* 预留 */
        private String data;

        /**
         * <p>
         * 预留对象，默认 fastjson 不参与序列化（也就是不存放在 cookie 中 ）
         * </p>
         * <p>
         * 此处配合分布式缓存使用，可以存放用户常用基本信息
         * </p>
         */
        @JSONField(serialize = false)
        private Object object;

        public SSOToken() {
            //this.setApp(SSOConfig.getInstance().getRole());
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        /**
         * 缓存用户信息，自动类型转换
         */
        @SuppressWarnings("unchecked")
        public <T> T getCacheObject() {
            return (T) this.getObject();
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    public static class Token {

    }
}
