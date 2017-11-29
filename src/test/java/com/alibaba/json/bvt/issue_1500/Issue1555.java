package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

public class Issue1555 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.userId = 1001;
        model.userName = "test";
        String text = JSON.toJSONString(model);
        assertEquals("{\"userName\":\"test\",\"user_id\":1001}", text);

        Model model2 = JSON.parseObject(text, Model.class);

        assertEquals(1001, model2.userId);
        assertEquals("test", model2.userName);
    }

    /**
     * 当某个字段有JSONField注解，JSONField中name属性不存在，json属性名也要用类上的属性名转换策略
     * @throws Exception
     */
    public void test_when_JSONField_have_not_name_attr() throws Exception {
        ModelTwo modelTwo = new ModelTwo();
        modelTwo.userId = 1001;
        modelTwo.userName = "test";
        String text = JSON.toJSONString(modelTwo);
        assertEquals("{\"userName\":\"test\",\"user_id\":\"1001\"}", text);

        Model model2 = JSON.parseObject(text, Model.class);

        assertEquals(1001, model2.userId);
        assertEquals("test", model2.userName);
    }

    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class Model {
        private int userId;
        @JSONField(name = "userName")
        private String userName;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class ModelTwo {
        /**
         * 此字段准备序列化为字符串类型
         */
        @JSONField(serializeUsing = StringSerializer.class)
        private int userId;
        @JSONField(name = "userName")
        private String userName;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
