package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wenshao on 04/08/2017.
 */
public class UUIDBenchmark {
    static String json = "{\"v1\":\"d3ab4486-f6c5-4419-8a06-12b0d5853cbe\",\"v2\":\"aae4fb77-e5b3-4e3a-b331-d2fbbf812d8f\",\"v3\":\"99717c8d-5c39-4f1b-b44c-548bf5dd6060\",\"v4\":\"6269e62f-67ba-463d-a38e-c568c25571fb\",\"v5\":\"62be2c96-472a-4253-938b-e71a131c1670\"}";
    static String json2 = "{\"v1\":\"d3ab4486f6c544198a0612b0d5853cbe\",\"v2\":\"aae4fb77e5b34e3ab331d2fbbf812d8f\",\"v3\":\"99717c8d5c394f1bb44c548bf5dd6060\",\"v4\":\"6269e62f67ba463da38ec568c25571fb\",\"v5\":\"62be2c96472a4253938be71a131c1670\"}";
    static String json_null = "{\"v1\":null,\"v2\":null,\"v3\":null,\"v4\":null,\"v5\":null}";
    static String json3 = "[\"d3ab4486-f6c5-4419-8a06-12b0d5853cbe\",\"aae4fb77-e5b3-4e3a-b331-d2fbbf812d8f\",\"99717c8d-5c39-4f1b-b44c-548bf5dd6060\",\"6269e62f-67ba-463d-a38e-c568c25571fb\",\"62be2c96-472a-4253-938b-e71a131c1670\"]";
    static String json4 = "[\"d3ab4486f6c544198a0612b0d5853cbe\",\"aae4fb77e5b34e3ab331d2fbbf812d8f\",\"99717c8d5c394f1bb44c548bf5dd6060\",\"6269e62f67ba463da38ec568c25571fb\",\"62be2c96472a4253938be71a131c1670\"]";
    static String json_null_array = "[null,null,null,null,null]";

    public static void main(String[] args) throws Exception {
//        Model model = new Model();
//        model.v1 = UUID.randomUUID();
//        model.v2 = UUID.randomUUID();
//        model.v3 = UUID.randomUUID();
//        model.v4 = UUID.randomUUID();
//        model.v5 = UUID.randomUUID();
//
//        System.out.println(JSON.toJSONString(model));


        for (int i = 0; i < 10; ++i) {
//             perf(); // 25021 24135 4977
//            perf2(); // 4276
//            perf_null(); // 4244 1278
//            perf3(); // 25247 3970
//            perf4(); // 3733
            perf_null_array(); // 746
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

    public static void perf_null() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json_null, Model.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf3() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json3, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf4() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json4, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static void perf_null_array() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000 * 10; ++i) {
            JSON.parseObject(json_null_array, Model.class, Feature.SupportArrayToBean);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("millis : " + millis);
    }

    public static class Model {
        public UUID v1;
        public UUID v2;
        public UUID v3;
        public UUID v4;
        public UUID v5;
    }
}
