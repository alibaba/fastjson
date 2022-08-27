package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class ByteBenchmark_obj {
    static String json = "{\"v1\":-55,\"v2\":67,\"v3\":107,\"v4\":96,\"v5\":-119}";
    static String json2 = "{\"v1\":\"-55\",\"v2\":\"67\",\"v3\":\"107\",\"v4\":\"96\",\"v5\":\"-119\"}";

    public static void main(String[] args) throws Exception {
        Model model = new Model();
        model.v1 = (byte) new Random().nextInt();
        model.v2 = (byte) new Random().nextInt();
        model.v3 = (byte) new Random().nextInt();
        model.v4 = (byte) new Random().nextInt();
        model.v5 = (byte) new Random().nextInt();

        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
             perf(); // 1398
//            perf2(); // 1467
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
        public Byte v1;
        public Byte v2;
        public Byte v3;
        public Byte v4;
        public Byte v5;
    }
}
