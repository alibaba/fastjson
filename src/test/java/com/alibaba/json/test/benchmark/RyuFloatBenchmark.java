package com.alibaba.json.test.benchmark;

import com.alibaba.fastjson.util.RyuDouble;
import com.alibaba.fastjson.util.RyuFloat;

public class RyuFloatBenchmark {
    private final static int COUNT = 1000 * 1000 * 10;
    public static void main(String[] args) throws Exception {
        float v = 0.539005056644f; //new java.util.Random().nextDouble();


        System.out.println(v);

//        for (int i = 0; i < 10; ++i) {
//            f0(v); // 741
//        }

//        System.out.println();
//
        for (int i = 0; i < 10; ++i) {
            f1(v); // 368
        }
    }

    public static void f0(float v) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            Float.toString(v);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("jdk : " + millis);
    }

    public static void f1(float v) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            RyuFloat.toString(v);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("ryu : " + millis);
    }
}
