package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

public class Issue1555 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.userId = 1001;

        String text = JSON.toJSONString(model);

        assertEquals("{\"user_id\":1001}", text);

        Model model2 = JSON.parseObject(text, Model.class);
        assertEquals(1001, model2.userId);
    }

    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class Model {
        private int userId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
