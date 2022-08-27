package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/02/2017.
 */
public class Issue_for_huangfeng extends TestCase {
    public void test_for_huangfeng() throws Exception {
        String json = "{\"success\":\"Y\"}";

        Model model = JSON.parseObject(json, Model.class);
        assertTrue(model.isSuccess());
    }

    public void test_for_huangfeng_t() throws Exception {
        String json = "{\"success\":\"T\"}";

        Model model = JSON.parseObject(json, Model.class);
        assertTrue(model.isSuccess());
    }

    public void test_for_huangfeng_is_t() throws Exception {
        String json = "{\"isSuccess\":\"T\"}";

        Model model = JSON.parseObject(json, Model.class);
        assertTrue(model.isSuccess());
    }

    public void test_for_huangfeng_false() throws Exception {
        String json = "{\"success\":\"N\"}";

        Model model = JSON.parseObject(json, Model.class);
        assertFalse(model.isSuccess());
    }

    public void test_for_huangfeng_false_f() throws Exception {
        String json = "{\"success\":\"F\"}";

        Model model = JSON.parseObject(json, Model.class);
        assertFalse(model.isSuccess());
    }

    public static class Model {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
