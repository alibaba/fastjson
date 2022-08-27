package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class ByteBenchmark_arrayMapping_obj {
    static String json = "[-55,67,107,96,-119]";
    static String json2 = "[\"-55\",\"67\",\"107\",\"96\",\"-119\"]";

    public static void main(String[] args) throws Exception {
        Model model = new Model();
        model.v1 = (byte) new Random().nextInt();
        model.v2 = (byte) new Random().nextInt();
        model.v3 = (byte) new Random().nextInt();
        model.v4 = (byte) new Random().nextInt();
        model.v5 = (byte) new Random().nextInt();

        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 818
            perf2(); // 903
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
        public Byte v1;
        public Byte v2;
        public Byte v3;
        public Byte v4;
        public Byte v5;
    }
}
