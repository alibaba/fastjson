package com.alibaba.json.test.benchmark;

import java.lang.management.ManagementFactory;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.TestUtils;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;

import data.media.MediaContent;

public class BenchmarkMain_EishayEncode {

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.vm.name") + " " + System.getProperty("java.runtime.version"));
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        System.out.println(arguments);

        MediaContent content = EishayDecodeBytes.instance.getContent();
        String text = encode(content);
        System.out.println(text);
        
        for (int i = 0; i < 10; ++i) {
            perf(text);
        }
    }

    static long perf(Object obj) {
        long startYGC = TestUtils.getYoungGC();
        long startYGCTime = TestUtils.getYoungGCTime();
        long startFGC = TestUtils.getFullGC();

        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            encode(obj);
        }
        long millis = System.currentTimeMillis() - startMillis;

        long ygc = TestUtils.getYoungGC() - startYGC;
        long ygct = TestUtils.getYoungGCTime() - startYGCTime;
        long fgc = TestUtils.getFullGC() - startFGC;

        System.out.println("encode\t" + millis + ", ygc " + ygc + ", ygct " + ygct + ", fgc " + fgc);
        return millis;
    }

    static String encode(Object text) {
        return JSON.toJSONString(text, SerializerFeature.BeanToArray);
    }
}
