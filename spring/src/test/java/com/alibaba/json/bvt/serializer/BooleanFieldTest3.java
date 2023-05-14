package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by wuwen on 2016/11/3.
 */
public class BooleanFieldTest3 extends TestCase {

    public void test_model() throws Exception {
        Model model = new Model();

        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"ok\":true,\"ok2\":true,\"ok3\":true}", text);
    }

    public static class Model {

        private Long fail;

        private boolean ok;

        private Boolean ok2;

        private boolean ok3;

        public Long isFail() {
            return 1L;
        }

        public boolean getOk() {
            return true;
        }

        public boolean isOk() {
            return false;
        }

        public Boolean getOk2() {
            return true;
        }

        public Boolean isOk2() {
            return false;
        }

        public boolean isOk3() {
            return true;
        }

    }
}
