package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class FloatBenchmark {
    static String json = "{\"v1\":0.012671709,\"v2\":0.6042485,\"v3\":0.13231707,\"v4\":0.80090785,\"v5\":0.6192943}";
    static String json2 = "{\"v1\":\"0.012671709\",\"v2\":\"0.6042485\",\"v3\":\"0.13231707\",\"v4\":\"0.80090785\",\"v5\":\"0.6192943\"}";
    static String json3 = "{\n" +
            "\t\"v1\":0.012671709,\n" +
            "\t\"v2\":0.6042485,\n" +
            "\t\"v3\":0.13231707,\n" +
            "\t\"v4\":0.80090785,\n" +
            "\t\"v5\":0.6192943\n" +
            "}";

    public static void main(String[] args) throws Exception {
        Model model = new Model();
//        model.v1 = new Random().nextFloat();
//        model.v2 = new Random().nextFloat();
//        model.v3 = new Random().nextFloat();
//        model.v4 = new Random().nextFloat();
//        model.v5 = new Random().nextFloat();
////
//        System.out.println(JSON.toJSONString(model));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json), true));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 210
//            perf2(); // 216
            perf3(); // 246
        }
    }

    public static void perf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.parseObject(json, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf2() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.parseObject(json2, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf3() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.parseObject(json3, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public float v1;
        public float v2;
        public float v3;
        public float v4;
        public float v5;
    }
}
