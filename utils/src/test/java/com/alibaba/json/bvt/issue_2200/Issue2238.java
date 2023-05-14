package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.math.BigDecimal;

public class Issue2238 extends TestCase {
    public void test_for_issue() throws Exception {
        CapitalLimitMonenyDTO capitalLimitMonenyDTO =new CapitalLimitMonenyDTO();
        capitalLimitMonenyDTO.setMaxChargeMoney(new BigDecimal("200000"));
        capitalLimitMonenyDTO.setMinChargeMoney(new BigDecimal(0.01));
        capitalLimitMonenyDTO.setMaxWithdrawMoney(new BigDecimal(0.01));
        capitalLimitMonenyDTO.setMinWithdrawMoney(new BigDecimal("500000"));
        System.out.println(JSON.toJSONString(capitalLimitMonenyDTO));
    }

    public static class CapitalLimitMonenyDTO {
        private BigDecimal maxChargeMoney;
        private BigDecimal minChargeMoney;
        private BigDecimal maxWithdrawMoney;
        private BigDecimal minWithdrawMoney;

        public BigDecimal getMaxChargeMoney() {
            return maxChargeMoney;
        }

        public void setMaxChargeMoney(BigDecimal maxChargeMoney) {
            this.maxChargeMoney = maxChargeMoney;
        }

        public BigDecimal getMinChargeMoney() {
            return minChargeMoney;
        }

        public void setMinChargeMoney(BigDecimal minChargeMoney) {
            this.minChargeMoney = minChargeMoney;
        }

        public BigDecimal getMaxWithdrawMoney() {
            return maxWithdrawMoney;
        }

        public void setMaxWithdrawMoney(BigDecimal maxWithdrawMoney) {
            this.maxWithdrawMoney = maxWithdrawMoney;
        }

        public BigDecimal getMinWithdrawMoney() {
            return minWithdrawMoney;
        }

        public void setMinWithdrawMoney(BigDecimal minWithdrawMoney) {
            this.minWithdrawMoney = minWithdrawMoney;
        }
    }
}
