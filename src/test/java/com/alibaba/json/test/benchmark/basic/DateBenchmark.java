package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wenshao on 04/08/2017.
 */
public class DateBenchmark {
    static String json = "{\"v1\":\"2017-08-16 04:29:00\",\"v2\":\"2017-08-16 04:29:00\",\"v3\":\"2017-08-16 04:29:00\",\"v4\":\"2017-08-16 04:29:00\",\"v5\":\"2017-08-16 04:29:00\"}";
    static String json2 = "{\"v1\":1502828940000,\"v2\":1502828940000,\"v3\":1502828940000,\"v4\":1502828940000,\"v5\":1502828940000}";
    static String json3 = "[\"2017-08-16 04:29:00\",\"2017-08-16 04:29:00\",\"2017-08-16 04:29:00\",\"2017-08-16 04:29:00\",\"2017-08-16 04:29:00\"]";
    static String json4 = "[1502828940000,1502828940000,1502828940000,1502828940000,1502828940000]";
    static String json_null_array = "[null,null,null,null,null]";

    public static void main(String[] args) throws Exception {
//        Model model = new Model();
//        model.v1 = new Random().nextDouble();
//        model.v2 = new Random().nextDouble();
//        model.v3 = new Random().nextDouble();
//        model.v4 = new Random().nextDouble();
//        model.v5 = new Random().nextDouble();
//
//        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 5; ++i) {
             perf(); // 18540
//            perf2(); // 2205
//            perf3(); // 22660 20963 18566
//            perf4(); // 2020
//            perf_null_array(); // 841
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

    public static void perf3() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json3, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf4() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json4, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf_null_array() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json_null_array, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public Date v1;
        public Date v2;
        public Date v3;
        public Date v4;
        public Date v5;
    }
}
