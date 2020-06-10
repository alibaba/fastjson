package com.alibaba.fastjson.serializer.issue3177;

/**
 *
 * @author shenzhou-6
 * @since  2020年05月26日
 *
 * https://github.com/alibaba/fastjson/issues/3177
 */
public class Test3177Bean {
    static class Parent {
        private String _status;

        public String get_status() {
            return _status;
        }

        public void set_status(String _status) {
            this._status = _status;
        }
    }

    static class Son extends Parent {

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
