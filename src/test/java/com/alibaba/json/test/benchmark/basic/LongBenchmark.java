package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class LongBenchmark {
    static String json = "{\"v1\":-1883391953414482124,\"v2\":-3019416596934963650,\"v3\":6497525620823745793,\"v4\":2136224289077142499,\"v5\":-2090575024006307745}";
    static String json2 = "{\"v1\":\"-1883391953414482124\",\"v2\":\"-3019416596934963650\",\"v3\":\"6497525620823745793\",\"v4\":\"2136224289077142499\",\"v5\":\"-2090575024006307745\"}";
    static String json3 = "{\n" +
            "\t\"v1\":-1883391953414482124,\n" +
            "\t\"v2\":-3019416596934963650,\n" +
            "\t\"v3\":6497525620823745793,\n" +
            "\t\"v4\":2136224289077142499,\n" +
            "\t\"v5\":-2090575024006307745\n" +
            "}";

    public static void main(String[] args) throws Exception {
//        Model model = new Model();
//        model.v1 = new Random().nextLong();
//        model.v2 = new Random().nextLong();
//        model.v3 = new Random().nextLong();
//        model.v4 = new Random().nextLong();
//        model.v5 = new Random().nextLong();
//
//        System.out.println(JSON.toJSONString(model));

//        System.out.println(JSON.toJSONString(JSON.parseObject(json), true));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 2799
//            perf2(); // 2931
            perf3(); // 10256
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
            JSON.parseObject(json3, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public long v1;
        public long v2;
        public long v3;
        public long v4;
        public long v5;
    }
}
