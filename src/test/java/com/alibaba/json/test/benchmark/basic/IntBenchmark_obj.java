package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

/**
 * Created by wenshao on 04/08/2017.
 */
public class IntBenchmark_obj {
    static String json = "{\"v1\":-1224609302,\"v2\":379420556,\"v3\":-1098099527,\"v4\":-2018662,\"v5\":422842162}";
    static String json2 = "{\"v1\":\"-1224609302\",\"v2\":\"379420556\",\"v3\":\"-1098099527\",\"v4\":\"-2018662\",\"v5\":\"422842162\"}";

    public static void main(String[] args) throws Exception {
//        Model model = new Model();
//        model.v1 = new Random().nextInt();
//        model.v2 = new Random().nextInt();
//        model.v3 = new Random().nextInt();
//        model.v4 = new Random().nextInt();
//        model.v5 = new Random().nextInt();
//
//        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 1888
            perf2(); // 1965
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
        public Integer v1;
        public Integer v2;
        public Integer v3;
        public Integer v4;
        public Integer v5;
    }
}
