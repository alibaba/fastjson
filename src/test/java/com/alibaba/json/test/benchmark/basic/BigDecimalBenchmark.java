package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

/**
 * Created by wenshao on 04/08/2017.
 */
public class BigDecimalBenchmark {
    static String json = "{\"v1\":0.4430165316544028,\"v2\":0.22676692048907365,\"v3\":0.9766986818812096,\"v4\":0.3423751102308744,\"v5\":0.4262177938610565}";
    static String json2 = "{\"v1\":\"0.4430165316544028\",\"v2\":\"0.22676692048907365\",\"v3\":\"0.9766986818812096\",\"v4\":\"0.3423751102308744\",\"v5\":\"0.4262177938610565\"}";

    public static void main(String[] args) throws Exception {
//        Model model = new Model();
//        model.v1 = new Random().nextDouble();
//        model.v2 = new Random().nextDouble();
//        model.v3 = new Random().nextDouble();
//        model.v4 = new Random().nextDouble();
//        model.v5 = new Random().nextDouble();
//
//        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
             perf(); // 8791 8437 7949 7771ll
//            perf2(); // 9908
        }
    }

    public static void perf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf2() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json2, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public BigDecimal v1;
        public BigDecimal v2;
        public BigDecimal v3;
        public BigDecimal v4;
        public BigDecimal v5;
    }
}
