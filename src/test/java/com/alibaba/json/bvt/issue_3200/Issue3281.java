package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;

import java.util.Date;
import java.util.HashMap;

/**
 * @Author ：Nanqi
 * @Date ：Created in 10:47 2020/6/22
 * @Description：补充测试用例
 */
public class Issue3281 extends TestCase {
    public void test_for_issue() {
        ModelState modelBack = JSON.parseObject("{\"counterMap\":{\"0\":0,\"100\":0,\"200\":0,\"300\":0,\"400\":0," +
                "\"500\":0,\"600\":0,\"700\":0,\"800\":0,\"900\":0,\"1000\":0},\"formatDate\":null," +
                "\"modelName\":\"test\",\"modelScores\":{\"Test1-1000\":{\"max\":1.0997832999999515,\"min\":0.0," +
                "\"recording\":false}},\"modelVersion\":\"1\",\"pit\":1592470429399,\"useCaseName\":\"test\"," +
                "\"variableName\":\"v2\"}", ModelState.class);
        Assert.assertNotNull(modelBack.getCounterMap());
        Assert.assertNotNull(modelBack.getModelScores());
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class ModelState {
        private HashMap<String, Long> counterMap;

        private Date formatDate;

        private HashMap<String, TGigest> modelScores;

        private String modelName;

        private Long modelVersion;

        private Long pit;

        private String useCaseName;

        private String variableName;
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class TGigest {
        private Double max;

        private Double min;

        private Boolean recording;
    }
}