package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

public class Bug_for_km
        extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\n" +
                "\t\"bizSubmitInfos\": [{\n" +
                "\t\t\"bizType\": \"ISSUE\",\n" +
                "\t\t\"privacyInfo\": \"{\\\"bcAnchorMid\\\":\\\"101CITI000000001000\\\",\\\"bcMid\\\":\\\"102BABA000000000001\\\",\\\"bcSubmitMid\\\":\\\"102BABA000000000001\\\",\\\"bizId\\\":\\\"transfer_autoTest_pri_weiweitestt_8857136636875877184\\\",\\\"bizType\\\":\\\"ISSUE\\\",\\\"currency\\\":\\\"USD\\\",\\\"exchangeRate\\\":{\\\"encryptedExchangeRate\\\":\\\"123\\\",\\\"envelopeKeyInfos\\\":{}},\\\"pcAmount\\\":{\\\"envelopeKeyInfos\\\":{},\\\"proof\\\":\\\"proff\\\"}}\",\n" +
                "\t\t\"submitInfoList\": [{\n" +
                "\t\t\t\"checkPrivacyResult\": {\n" +
                "\t\t\t\t\"checkResult\": \"{\\\"checkResultStatus\\\":\\\"ACCEPT\\\",\\\"hashOfPrivacyPreservingInfo\\\":\\\"NfYRZwG/Yki8LU01vsyWINGaXv94+gYoPBFVMFEKyTM=\\\",\\\"memberId\\\":\\\"101CITI000000001000\\\"}\",\n" +
                "\t\t\t\t\"signature\": \"testing_signature\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"pid\": \"101CITI000000001000\"\n" +
                "\t\t}]\n" +
                "\t}],\n" +
                "\t\"resultInfo\": {\n" +
                "\t\t\"resultCode\": \"SUCCESS\",\n" +
                "\t\t\"resultCodeId\": \"00000000\",\n" +
                "\t\t\"resultMsg\": \"success\",\n" +
                "\t\t\"resultStatus\": \"S\"\n" +
                "\t}\n" +
                "}";

        WhaleGeneratePrivacyResponseBody resp = JSON.parseObject(str, WhaleGeneratePrivacyResponseBody.class);
        System.out.println(resp.resultInfo.resultStatus);

//        System.out.println(str);
    }


    public static class WhaleGeneratePrivacyResponseBody {
        public ResultInfo resultInfo;
    }

    /**
     * @author freud.wy
     * @version $Id: ResultInfo.java, v 0.1 2019-05-28 上午11:02 freud.wy Exp $$
     */
    public static class ResultInfo {
        private String resultStatus;
        private String resultCodeId;
        private String resultCode;
        private String resultMsg;

        /**
         * Getter method for property <tt>resultStatus</tt>.
         *
         * @return property value of resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * Setter method for property <tt>resultStatus</tt>.
         *
         * @param resultStatus value to be assigned to property resultStatus
         */
        public void setResultStatus(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        /**
         * Getter method for property <tt>resultCodeId</tt>.
         *
         * @return property value of resultCodeId
         */
        public String getResultCodeId() {
            return resultCodeId;
        }

        /**
         * Setter method for property <tt>resultCodeId</tt>.
         *
         * @param resultCodeId value to be assigned to property resultCodeId
         */
        public void setResultCodeId(String resultCodeId) {
            this.resultCodeId = resultCodeId;
        }

        /**
         * Getter method for property <tt>resultCode</tt>.
         *
         * @return property value of resultCode
         */
        public String getResultCode() {
            return resultCode;
        }

        /**
         * Setter method for property <tt>resultCode</tt>.
         *
         * @param resultCode value to be assigned to property resultCode
         */
        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        /**
         * Getter method for property <tt>resultMsg</tt>.
         *
         * @return property value of resultMsg
         */
        public String getResultMsg() {
            return resultMsg;
        }

        /**
         * Setter method for property <tt>resultMsg</tt>.
         *
         * @param resultMsg value to be assigned to property resultMsg
         */
        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }

        @Override
        public String toString() {
            return String.format("resultStatus[%s],resultCodeId[%s],resultCode[%s],resultMsg[%s]",resultStatus,resultCodeId,resultCode,resultMsg);
        }
    }
}
