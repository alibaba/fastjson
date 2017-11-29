package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class BooleanBenchmark {
    static String json = "{\"v1\":true,\"v2\":false,\"v3\":true,\"v4\":false,\"v5\":false}";
    static String json2 = "{\"v1\":\"true\",\"v2\":\"false\",\"v3\":\"true\",\"v4\":\"false\",\"v5\":\"false\"}";
    static String json3 = "{\"v1\":1,\"v2\":0,\"v3\":1,\"v4\":0,\"v5\":1}";
    static String json4 = "{\"v1\":\"1\",\"v2\":\"0\",\"v3\":\"1\",\"v4\":\"0\",\"v5\":\"1\"}";
    static String json5 = "{\n" +
            "\t\"v1\":true,\n" +
            "\t\"v2\":false,\n" +
            "\t\"v3\":true,\n" +
            "\t\"v4\":false,\n" +
            "\t\"v5\":false\n" +
            "}";

    public static void main(String[] args) throws Exception {
//        System.out.println(JSON.toJSONString(JSON.parseObject(json), true));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 1266
//            perf2(); // 1334
//            perf3(); // 1085
//            perf4(); // 1085
            perf5(); // 1803
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

    public static void perf4() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json4, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf5() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json5, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public boolean v1;
        public boolean v2;
        public boolean v3;
        public boolean v4;
        public boolean v5;
    }
}
