package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2635 extends TestCase {
    public void testForIssue() throws Exception {
        String json = "{\"dt\":\"evt\",\"pr\":{\"_订单金额\":\"100\",\"$AA_epid#_优惠券金额\":848,\"$AA_eptp#_Client_id\":\"string\",\"$AA_eptp#_访客类别\":\"string\",\"$AA_uid\":856,\"$AA_eptp#_优惠券类型\":\"string\",\"$AA_sid\":1565851940554,\"$AA_eptp#_优惠券金额\":\"string\",\"$AA_epid#_订单金额\":855,\"$AA_epid#_订单号\":847,\"_优惠券名称\":\"60元优惠折扣券\",\"$eid\":\"小程序_订单确认\",\"$AA_eptp#_订单渠道\":\"string\",\"$ct\":1565854057608,\"$cuid\":\"YYYY\",\"_Experiment_id\":\"0\",\"$AA_epid#_优惠券类型\":853,\"_优惠券类型\":\"D4\",\"_店主ID\":\"53890475\",\"_优惠券金额\":\"100\",\"$AA_eptp#_Experiment_id\":\"string\",\"$AA_epid#_优惠券名称\":850,\"$AA_eptp#_优惠券名称\":\"string\",\"_优惠券 ID\":\"916090\",\"$AA_epid#_店主ID\":851,\"$tz\":28800000,\"$AA_eptp#_优惠券 ID\":\"string\",\"$AA_eptp#_订单号\":\"string\",\"$AA_AAid\":7097,\"$AA_eptp#_店主ID\":\"string\",\"$AA_eid\":175,\"$AA_epid#_Client_id\":21544,\"$sid\":1569851940554,\"_订单渠道\":\"云购小程序\",\"_Client_id\":\"1e8e82fe-c90f-f363-6693-143677891dfa\",\"$AA_epid#_事件类型\":3073,\"$AA_epid#_分享来源用户\":14694,\"$AA_epid#_Experiment_id\":21543,\"_分享来源用户\":\"53890475\",\"$AA_eptp#_订单金额\":\"string\",\"$AA_epid#_订单渠道\":852,\"$AA_eptp#_分享来源用户\":\"string\",\"$url\":\"http://171.90.15:87/CCTesting/data/toPrivateTest?test=https://u2.CCio.com/CC.js&appkey=b8868018cIO94114ad7a81cd5f1ddafd\",\"_访客类别\":\"ABO\",\"$AA_epid#_优惠券 ID\":854,\"_订单号\":\"PP190830000683\",\"_下单用户\":\"720003734\",\"$AA_epid#_下单用户\":849,\"$AA_eptp#_事件类型\":\"string\",\"$uuid\":\"5c910d893bc341aBHN02119708ec13df\",\"$AA_eptp#_下单用户\":\"string\",\"$AA_epid#_访客类别\":856,\"$AA_did\":6736,\"$referrer_domain\":\"10.33.180.15:8088\",\"_事件类型\":\"订单确认\",\"$ref\":\"http://10.283.100.10:8088/CCTesting/data/toPrivate\"}}\n";
        JSON.parseObject(json);
    }
}
