package com.alibaba.json.test.benchmark;

import java.lang.management.ManagementFactory;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.TestUtils;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;

import data.media.MediaContent;

public class BenchmarkMain_EishayDecode {

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.vm.name") + " " + System.getProperty("java.runtime.version"));
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        System.out.println(arguments);

        String text = EishayDecodeBytes.instance.getText();
        System.out.println(text);
        
        for (int i = 0; i < 10; ++i) {
            perf(text);
        }
    }

    static long perf(String text) {
        long startYGC = TestUtils.getYoungGC();
        long startYGCTime = TestUtils.getYoungGCTime();
        long startFGC = TestUtils.getFullGC();

        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            decode(text);
        }
        long millis = System.currentTimeMillis() - startMillis;

        long ygc = TestUtils.getYoungGC() - startYGC;
        long ygct = TestUtils.getYoungGCTime() - startYGCTime;
        long fgc = TestUtils.getFullGC() - startFGC;

        System.out.println("decode\t" + millis + ", ygc " + ygc + ", ygct " + ygct + ", fgc " + fgc);
        return millis;
    }

    static void decode(String text) {
        MediaContent content = JSON.parseObject(text, MediaContent.class);
        
//        JSON.parseObject(text);
    }
}
