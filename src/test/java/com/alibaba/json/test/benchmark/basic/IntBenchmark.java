package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class IntBenchmark {
    static String json = "{\"v1\":-1224609302,\"v2\":379420556,\"v3\":-1098099527,\"v4\":-2018662,\"v5\":422842162}";
    static String json2 = "{\"v1\":\"-1224609302\",\"v2\":\"379420556\",\"v3\":\"-1098099527\",\"v4\":\"-2018662\",\"v5\":\"422842162\"}";
    static String json3 = "{\n" +
            "\t\"v1\":\"-1224609302\",\n" +
            "\t\"v2\":\"379420556\",\n" +
            "\t\"v3\":\"-1098099527\",\n" +
            "\t\"v4\":\"-2018662\",\n" +
            "\t\"v5\":\"422842162\"\n" +
            "}";

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.vm.name") + " " + System.getProperty("java.runtime.version"));
//        Model model = new Model();
//        model.v1 = new Random().nextInt();
//        model.v2 = new Random().nextInt();
//        model.v3 = new Random().nextInt();
//        model.v4 = new Random().nextInt();
//        model.v5 = new Random().nextInt();
//
//        System.out.println(JSON.toJSONString(model));


        for (int i = 0; i < 10; ++i) {
             perf(); // 1798
           // perf2(); // 1877
//            perf3(); // 20624 2334
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
        public int v1;
        public int v2;
        public int v3;
        public int v4;
        public int v5;
    }
}
