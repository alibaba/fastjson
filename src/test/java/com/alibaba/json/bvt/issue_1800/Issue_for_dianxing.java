package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue_for_dianxing extends TestCase {
    public void test_0() throws Exception {
        String s = "{\"alarmLevel\":null,\"error\":null,\"errorCount\":0,\"maxAlarmCount\":10,\"warn\":null,"
                + "\"warnCount\":0}";
        TopAlarm b = JSON.parseObject(s, TopAlarm.class);
        System.out.println(JSON.toJSONString(b));
    }

    public static class TopAlarm {

        private Double error;                                  //为null表示不报警
        private int errorCount;
        private Double warn;                                   //为null表示不报警
        private int warnCount;
        private Integer alarmLevel;
        private int maxAlarmCount = 10;

        public TopAlarm() {
        }

        public Double getError() {
            return error;
        }

        public void setError(Double error) {
            this.error = error;
        }

        public Double getWarn() {
            return warn;
        }

        public void setWarn(Double warn) {
            this.warn = warn;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }

        public int getWarnCount() {
            return warnCount;
        }

        public void setWarnCount(int warnCount) {
            this.warnCount = warnCount;
        }

        public Integer getAlarmLevel() {
            return alarmLevel;
        }

        public void setAlarmLevel(Integer alarmLevel) {
            this.alarmLevel = alarmLevel;
        }

        public int getMaxAlarmCount() {
            return maxAlarmCount;
        }

        public void setMaxAlarmCount(int maxAlarmCount) {
            this.maxAlarmCount = maxAlarmCount;
        }

    }
}
