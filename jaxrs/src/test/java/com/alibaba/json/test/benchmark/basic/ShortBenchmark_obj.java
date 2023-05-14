package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * Created by wenshao on 04/08/2017.
 */
public class ShortBenchmark_obj {
    static String json = "{\"v1\":-5972,\"v2\":5582,\"v3\":-2398,\"v4\":-9859,\"v5\":25417}";
    static String json2 = "{\"v1\":\"-5972\",\"v2\":\"5582\",\"v3\":\"-2398\",\"v4\":\"-9859\",\"v5\":\"25417\"}";

    public static void main(String[] args) throws Exception {
        Model model = new Model();
        model.v1 = (short) new Random().nextInt();
        model.v2 = (short) new Random().nextInt();
        model.v3 = (short) new Random().nextInt();
        model.v4 = (short) new Random().nextInt();
        model.v5 = (short) new Random().nextInt();

        System.out.println(JSON.toJSONString(model));

        for (int i = 0; i < 10; ++i) {
//             perf(); // 1527
            perf2(); // 1667
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
        public Short v1;
        public Short v2;
        public Short v3;
        public Short v4;
        public Short v5;
    }
}
