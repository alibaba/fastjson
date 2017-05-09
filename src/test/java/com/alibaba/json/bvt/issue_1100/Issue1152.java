package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wenshao on 08/05/2017.
 */
public class Issue1152 extends TestCase {
    public void test_for_issue() throws Exception {
        TestBean tb = JSONObject.parseObject("{shijian:\"0000-00-00T00:00:00\"}",TestBean.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertNull(tb.getShijian());
    }

    public static class TestBean {

        private Date shijian;

        public Date getShijian() {
            return shijian;
        }
        @JSONField(name="shijian" )
        public void setShijian(Date shijian) {
            this.shijian = shijian;
        }
    }
}
