package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class ShortBenchmark_arrayMappinng_obj {
    static String json = "[-5972,5582,-2398,-9859,25417]";
    static String json2 = "[\"-5972\",\"5582\",\"-2398\",\"-9859\",\"25417\"]";

    public static void main(String[] args) throws Exception {
        Model model = new Model();
        model.v1 = (short) new Random().nextInt();
        model.v2 = (short) new Random().nextInt();
        model.v3 = (short) new Random().nextInt();
        model.v4 = (short) new Random().nextInt();
        model.v5 = (short) new Random().nextInt();

        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
             perf(); // 1087
//            perf2(); // 1120
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
        public Short v1;
        public Short v2;
        public Short v3;
        public Short v4;
        public Short v5;
    }
}
