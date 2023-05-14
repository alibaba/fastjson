package com.alibaba.json.bvt.lombok;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class LomBokTest extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\n" +
                "\t\"target\": 1,\n" +
                "\t\"current\": 0,\n" +
                "\t\"step\": 1,\n" +
                "\t\"uqcRule\": {\n" +
                "\t\t\"ruleCode\": \"IND#PAY1212#NP1D#1\"\n" +
                "\t},\n" +
                "\t\"cycleRule\": {\n" +
                "\t\t\"decision\": {\"@type\": \"com.alibaba.json.bvt.lombok.LomBokTest$DaysCycleExeDecision\",\"days\": 1\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"dataSource\": {\n" +
                "\t\t\"PAYLINK\": {\n" +
                "\t\t\t\"target\": 0\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        ParserConfig.getGlobalInstance().addAccept("com.alibaba.json.bvt.lombok.LomBokTest.DaysCycleExeDecision");
        JSONObject obj = JSON.parseObject(str);
        IndicatorCycleRule cycleRule = obj.getObject("cycleRule", IndicatorCycleRule.class);
        System.out.println(((DaysCycleExeDecision) cycleRule.decision).days);
    }

    @lombok.Data
    public static class DaysCycleExeDecision implements ExeDecision {
        private int days;
    }

    public static interface ExeDecision {

    }

    @lombok.Data
    public static class IndicatorCycleRule {

        /**
         * 周期决策器
         */
        private ExeDecision decision;


    }
}
