package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;

public class Issue2300 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"createTime\":1548166745}";

        Order o = JSON.parseObject(text, Order.class);
        assertEquals(1548166745000L, o.createTime.getTime());

        String json = JSON.toJSONString(o);
        assertEquals("{\"createTime\":1548166745}", json);

        //新增校验1
        JSONObject jsonObject = JSONObject.parseObject(text);
        Order order1 = JSONObject.toJavaObject(jsonObject, Order.class);
        //校验不通过
        assertEquals(1548166745000L, order1.createTime.getTime());

        //新增校验2
        Order order2 = jsonObject.toJavaObject(Order.class);
        //校验不通过
        assertEquals(1548166745000L, order2.createTime.getTime());
    }

    public static class Order {
        @JSONField(format = "unixtime")
        public Date createTime;
    }
}
