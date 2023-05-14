package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

public class Bug_for_rd extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{ \"sitePayId\": \"2019071889031100000152604119889\", \"extendInfo\": \"{\\\"saveAsset\\\":\\\"false\\\",\\\"acquirementId\\\":\\\"20190718194010800100177150204979354\\\",\\\"orderTerminalType\\\":\\\"WEB\\\",\\\"acqSiteUserId\\\":\\\"2177220032166157\\\",\\\"siteReqBizId\\\":\\\"111023437\\\",\\\"msisdn\\\":\\\"01966114400\\\",\\\"originalMerchantOrderId\\\":\\\"602109378031994\\\"}\", \"netPayId\": \"2019071819039101720000454701\", \"resultInfo\": { \"resultCode\": \"SUCCESS\", \"resultCodeId\": \"00000000\", \"resultMsg\": \"Success\", \"resultStatus\": \"S\" } }";
        PayResponse object = JSON.parseObject(json, PayResponse.class);
        String extendInfo = object.getExtendInfo();

        assertEquals(object.getExtendInfo(), JSON.parseObject(json).get("extendInfo"));
    }

    public void test_for_issue_1() throws Exception {
        String json = "{\"unit\": \"MINUTES\"}";
        V1 v = JSON.parseObject(json, V1.class);
        assertEquals(TimeUnit.MINUTES, v.unit);
    }

    public void test_for_issue_2() throws Exception {
        String json = "{\"sitePayId\": \"MINUTES\"}";
        V1 v = JSON.parseObject(json, V1.class);
        assertEquals("MINUTES", v.sitePayId);
    }

    public void test_for_issue_3() throws Exception {
        String json = "{\"sitePayId\": \"MINUTES\\\"A\"}";
        V1 v = JSON.parseObject(json, V1.class);
        assertEquals("MINUTES\"A", v.sitePayId);
    }

    public void test_for_issue_4() throws Exception {
        String json = "{   \"sitePayId\": \"MINUTES\\\"A\"}";
        V1 v = JSON.parseObject(json, V1.class);
        assertEquals("MINUTES\"A", v.sitePayId);
    }

    public void test_for_issue_5() throws Exception {
        String json = "{   \"sitePayId\": \"\\\"A\"}";
        V1 v = JSON.parseObject(json, V1.class);
        assertEquals("\"A", v.sitePayId);
    }

    public void test_for_issue_6() throws Exception {
        String json = "{   \"sitePayId\": \"S\"}";
        V1 v = JSON.parseObject(json, V1.class);
        assertEquals("S", v.sitePayId);
    }

    public static class PayResponse {
        private String            sitePayId;
        private String            extendInfo;
        private String            netPayId;

        public String getSitePayId()
        {
            return sitePayId;
        }

        public void setSitePayId(String sitePayId)
        {
            this.sitePayId = sitePayId;
        }

        public String getExtendInfo()
        {
            return extendInfo;
        }

        public void setExtendInfo(String extendInfo)
        {
            this.extendInfo = extendInfo;
        }

        public String getNetPayId()
        {
            return netPayId;
        }

        public void setNetPayId(String netPayId)
        {
            this.netPayId = netPayId;
        }
    }

    public static class V1 {
        public String            sitePayId;
        public TimeUnit unit;
    }
}
