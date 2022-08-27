package com.alibaba.json.test.benchmark.basic;

import com.alibaba.fastjson.JSON;

import java.util.LinkedList;

/**
 * Created by wenshao on 06/08/2017.
 */
public class LinkedListBenchmark {
    public static void main(String[] args) throws Exception {
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < 1000; ++i) {
            linkedList.add(i);
        }

        for (int i = 0; i < 10; i++) {
            perf_toJSONString(linkedList); // 14825
        }
    }

    public static void perf_toJSONString(Object obj) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.toJSONString(obj);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("milli : " + millis);
    }
}
