package com.alibaba.json.test.benchmark;

import com.alibaba.fastjson.util.RyuDouble;

public class RyuDoubleBenchmark {
    private final static int COUNT = 1000 * 1000 * 10;
    public static void main(String[] args) throws Exception {
        double v = 0.5390050566444644; //new java.util.Random().nextDouble();


        System.out.println(v);

        for (int i = 0; i < 10; ++i) {
            f0(v); // 2505, 1865
        }

//        System.out.println();
//
//        for (int i = 0; i < 10; ++i) {
//            f1(v); // 752, 571
//        }
    }

    public static void f0(double v) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            Double.toString(v);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("jdk : " + millis);
    }

    public static void f1(double v) throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            RyuDouble.toString(v);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("ryu : " + millis);
    }
}
