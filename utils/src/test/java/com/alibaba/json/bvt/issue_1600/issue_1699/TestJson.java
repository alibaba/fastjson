package com.alibaba.json.bvt.issue_1600.issue_1699;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import java.io.Serializable;

public class TestJson extends TestCase {

    public void test_for_issue() {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);

        System.out.println(JSON.VERSION);

        String event1 = "{\"@type\":\"com.alibaba.json.bvt.issue_1600.issue_1699.obj.RatingDetailBO\",\"amount\":285.600000,\"billId\":3945,\"bizId\":\"6000007==201712==USER_ID==2049884395&&CONTRACT_NO==\\\"no1513922344271\\\"\",\"bizTime\":\"2017-12-31 00:00:00\",\"bizType\":\"6000007\",\"currency\":\"CNY\",\"dealTime\":\"2017-12-23 14:11:03\",\"detailType\":\"CYCLE_CHARGING\",\"extendInfo\":{\"@type\":\"java.util.LinkedHashMap\",\"BUY_AMOUNT\":\"3\",\"P_BIZ_ID\":\"USER_ID==2049884395&&CONTRACT_NO==\\\"no1513922344271\\\"\",\"SETTLE_SIDE\":\"654321\",\"SETTLE_CYCLE_TYPE\":\"3\",\"AUCTION_PRICE\":\"119\",\"CALCULATE_RANGE\":\"STORE\",\"TOTAL_NUM\":\"1\",\"BILL_CYCLE\":\"201712\",\"IS_PRE_CHARGING\":\"false\",\"BRANCH_SHOP\":\"branchShop1\",\"CONTRACT_TYPE\":\"HEMA_CHARGING_PROD\",\"stepRateType\":\"3\",\"SOURCE_TYPE\":\"PURCHASE_ADJUST\",\"SETTLE_SIDE_NICK\":\"测试结算主体\",\"express_value\":\"USER_ID==2049884395&&CONTRACT_NO==\\\"no1513922344271\\\"\",\"BIZ_TIME\":\"2017-12-22 13:59:05\",\"TRADE_ID\":\"1513922344273\",\"QUANTITY\":\"3.000000\",\"MES_RECEIVE_TIME\":\"2017-12-22 13:59:05\",\"UN_TAX_UNIT_PRICE\":\"100.000000\",\"AUCTION_ID\":\"123\",\"AUCTION_NAME\":\"测试商品\",\"rate_value\":\"{\\\"extendInfo\\\":{},\\\"intervalValues\\\":[{\\\"max\\\":600.000000,\\\"min\\\":0.000000,\\\"rate\\\":0.600000},{\\\"max\\\":1000.000000,\\\"min\\\":600.000000,\\\"rate\\\":0.300000},{\\\"max\\\":999999999999.000000,\\\"min\\\":1000.000000,\\\"rate\\\":0.100000}]}\",\"CAT_ID\":\"16\",\"UNIT\":\"kilometer\",\"TERM_NAME\":\"盒马.合同返利.促销推广费\",\"USER_ID\":\"2049884395\",\"UNIT_PRICE\":\"119.000000\",\"tbRuleCode\":\"HM_SETTLE_CHARGING\",\"AMOUNT\":\"357.000000\",\"CAT_NAME\":\"水果\",\"EXTERNAL_NO\":\"HM==1513922344273\",\"CHANNEL\":\"online\",\"is_default_rate\":\"false\",\"CURRENCY\":\"CNY\",\"rate_rule_id\":\"300000531\",\"OTHER_USER_NICK\":\"甲方\",\"RATE_TYPE\":\"14\",\"ITEM_NAME\":\"盒马.促销推广费\",\"rate_rule_inst_id\":\"1009129180821\",\"TAX_RATE\":\"0.190000\",\"ITEM_CODE\":\"BILL_HM_6000007\",\"CONTRACT_SIDE\":\"12345\",\"UNTAX_AMOUNT\":\"300.000000\",\"CONTRACT_VERSION\":\"V001\",\"CONTRACT_NO\":\"no1513922344271\",\"P_TRADE_ID\":\"1513922344273\"},\"gmtCreate\":\"2017-12-23 14:11:03\",\"gmtModified\":\"2017-12-23 14:11:03\",\"id\":6235300020395,\"indexNum\":0,\"innerId\":6300120395,\"innerTable\":\"SETTLE_DATA\",\"isJoin\":\"FALSE\",\"itemId\":90000000007031,\"mesId\":3235,\"mesReceiveTime\":\"2017-12-22 13:59:05\",\"outBizId\":\"USER_ID==2049884395&&CONTRACT_NO==\\\"no1513922344271\\\"\",\"pTradeId\":3235,\"priority\":0,\"proration\":0.6,\"quantity\":476.000000,\"rateDefineId\":40000443,\"rateParams\":{\"@type\":\"java.util.LinkedHashMap\"},\"status\":\"SUCCESS\",\"tradeId\":3761,\"userId\":2049884395,\"userNick\":\"乙方\",\"version\":1}";
        Serializable obj = JSON.parseObject(event1, Serializable.class, config);
        System.out.println(obj);
    }
}
