package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 08/01/2017.
 */
public class SerializeEnumAsJavaBeanTest_private extends TestCase {
    public void test_serializeEnumAsJavaBean() throws Exception {
        String text = JSON.toJSONString(OrderType.PayOrder);
        assertEquals("{\"remark\":\"支付订单\",\"value\":1}", text);
    }

    public void test_field() throws Exception {
        Model model = new Model();
        model.orderType = OrderType.SettleBill;
        String text = JSON.toJSONString(model);
        assertEquals("{\"orderType\":{\"remark\":\"结算单\",\"value\":2}}", text);
    }

    public void test_field_2() throws Exception {
        Model model = new Model();
        model.orderType = OrderType.SettleBill;
        model.orderType1 = OrderType.SettleBill;
        String text = JSON.toJSONString(model);
        assertEquals("{\"orderType\":{\"remark\":\"结算单\",\"value\":2},\"orderType1\":{\"remark\":\"结算单\",\"value\":2}}", text);
    }

    @JSONType(serializeEnumAsJavaBean = true)
    private static enum OrderType {
        PayOrder(1, "支付订单"), //
        SettleBill(2, "结算单");

        public final int value;
        public final String remark;

        private OrderType(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }

    private static class Model {
        public OrderType orderType;
        public OrderType orderType1;
    }
}
