package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue_for_gaorui extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"@type\":\"java.util.HashMap\",\"COUPON\":[{\"@type\":\"com.alibaba.json.bvt.issue_1600.Issue_for_gaorui.PromotionTermDetail\",\"activityId\":\"1584034\",\"choose\":true,\"couponId\":1251068987,\"couponType\":\"limitp\",\"match\":true,\"realPrice\":{\"amount\":0.6,\"currency\":\"USD\"}}],\"grayTrade\":\"true\"}";

        JSON.parseObject(json, Object.class, Feature.SupportAutoType);
    }

    public static class PromotionTermDetail {
        /**
         * 卡券Id
         */
        private Long couponId;
        /**
         * 营销Id
         */
        private String promotionId;
        /**
         * 实际单价
         */
        private Money realPrice;
        /**
         * 活动Id
         */
        private String activityId;

        /**
         * 卡券类型
         */
        private String couponType;

        /**
         * 是否能够获取到该优惠
         */
        private boolean isMatch = false;
        /**
         * 是否选择了该优惠
         */
        private boolean isChoose = false;
        /**
         * 未获取到优惠的原因
         */
        private String reasonForLose;
        /**
         * 未获取优惠的标识码
         */
        private String codeForLose;

        public Long getCouponId() {
            return couponId;
        }

        public void setCouponId(Long couponId) {
            this.couponId = couponId;
        }

        public String getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
        }

        public Money getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(Money realPrice) {
            this.realPrice = realPrice;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public String getCouponType() {
            return couponType;
        }

        public void setCouponType(String couponType) {
            this.couponType = couponType;
        }

        public boolean isMatch() {
            return isMatch;
        }

        public void setMatch(boolean match) {
            isMatch = match;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public String getReasonForLose() {
            return reasonForLose;
        }

        public void setReasonForLose(String reasonForLose) {
            this.reasonForLose = reasonForLose;
        }

        public String getCodeForLose() {
            return codeForLose;
        }

        public void setCodeForLose(String codeForLose) {
            this.codeForLose = codeForLose;
        }
    }

    public static class Money {

    }
}
