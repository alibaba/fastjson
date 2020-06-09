package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import lombok.Data;

public class Issue3246 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonStr = "{\"d_id\":\"bphyean01\",\"isOpenMergeCode\":0,\"offlineOrder\":false,\"offlineOrderType\":-1,\"og\":0,\"pushIdFromRemote\":false,\"qrisAmountPrice\":22000,\"s_req\":0,\"s_t\":1,\"skr_id\":0,\"type\":1,\"c_id\":471,\"o_$\":5500.0,\"am\":4,\"$_tp\":\"bp\",\"o_t\":1,\"a_m\":3}";
        Order parseOrder = JSON.parseObject(jsonStr,Order.class);
        assertEquals(Integer.valueOf(4), parseOrder.getAmount());
        assertEquals("3", parseOrder.getAddMoney());

    }

    @Data
    public static class Order {
        @JSONField(name = "d_id", ordinal = 0)
        private String deviceId;
        @JSONField(name = "c_id", ordinal = 1)
        private Integer commodityId;
        @JSONField(name = "o_$", ordinal = 2)
        private Double orderPrice;
        @JSONField(name = "am", ordinal = 3)
        private Integer amount;
        @JSONField(name = "$_tp", ordinal = 4)
        private String payType;
        @JSONField(name = "wx_p_id", ordinal = 5)
        private Long productId;
        @JSONField(name = "ext_p_id", ordinal = 6)
        private Long extraProductId;
        @JSONField(name = "u_id", ordinal = 7)
        private String userId;
        @JSONField(name = "p_id", ordinal = 8)
        private Long parentId;
        @JSONField(name = "o_t", ordinal = 9)
        private Integer orderType;
        @JSONField(name = "ts", ordinal = 10)
        private Integer tradeStatus;
        @JSONField(name = "pn", ordinal = 11)
        private String phoneNum;
        @JSONField(name = "conf_id", ordinal = 12)
        private Long configId;
        @JSONField(name = "sku_id", ordinal = 13)
        private Long skuCommodityId;
        @JSONField(name = "c_ids", ordinal = 14)
        private String commodityIds;
        @JSONField(name = "a_m", ordinal = 15)
        private String addMoney;
        @JSONField(name = "skr_id", ordinal = 15)
        private Long secKillRecordId;
        @JSONField(name = "c_n", ordinal = 16)
        private String clientOrderNum;
        @JSONField(name = "s_t", ordinal = 16)
        private Integer sceneType;
        @JSONField(name = "t_t", ordinal = 16)
        private Integer tradingType;

    }
}
