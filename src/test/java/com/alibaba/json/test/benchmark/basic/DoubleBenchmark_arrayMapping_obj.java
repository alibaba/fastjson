package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * Created by wenshao on 04/08/2017.
 */
public class DoubleBenchmark_arrayMapping_obj {
    static String json = "[0.4430165316544028,0.22676692048907365,0.9766986818812096,0.3423751102308744,0.4262177938610565]";
    static String json2 = "[\"0.4430165316544028\",\"0.22676692048907365\",\"0.9766986818812096\",\"0.3423751102308744\",\"0.4262177938610565\"]";

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
             perf(); // 320
//            perf2(); // 330
        }
    }

    public static void perf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf2() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json2, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public Double v1;
        public Double v2;
        public Double v3;
        public Double v4;
        public Double v5;
    }
}
