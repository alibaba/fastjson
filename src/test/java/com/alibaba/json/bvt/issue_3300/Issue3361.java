package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;


/**
 * @Author ：Nanqi
 * @Date ：Created in 23:43 2020/7/24
 */
public class Issue3361 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.setDate(new Date(1595645548002L));
        String jsonStr = JSONObject.toJSONString(model);
        model = JSONObject.parseObject(jsonStr, Model.class);
        assertEquals("Sat Jul 25 10:52:28 CST 2020", model.getDate().toString());
    }

    public void test_for_issue_02() throws Exception {
        Model model = new Model();
        model.setDate(new Date(1595645548002L));

        String jsonStr = JSON.toJSONStringWithDateFormat(model, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        model = JSON.parseObject(jsonStr, Model.class);

        assertEquals("Sat Jul 25 10:52:28 CST 2020", model.getDate().toString());
    }

    static class Model {
        @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
