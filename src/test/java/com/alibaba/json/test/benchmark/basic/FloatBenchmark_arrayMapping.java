package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * Created by wenshao on 04/08/2017.
 */
public class FloatBenchmark_arrayMapping {
    static String json = "[0.012671709,0.6042485,0.13231707,0.80090785,0.6192943]";
    static String json2 = "[\"0.012671709\",\"0.6042485\",\"0.13231707\",\"0.80090785\",\"0.6192943\"]";

    public static void main(String[] args) throws Exception {
        Model model = new Model();
//        model.v1 = new Random().nextFloat();
//        model.v2 = new Random().nextFloat();
//        model.v3 = new Random().nextFloat();
//        model.v4 = new Random().nextFloat();
//        model.v5 = new Random().nextFloat();
////
//        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 145
            perf2(); // 160
        }
    }

    public static void perf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.parseObject(json, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf2() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.parseObject(json2, Model.class, Feature.SupportArrayToBean);
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
