package com.alibaba.json.test.benchmark;

import java.lang.management.ManagementFactory;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.json.test.TestUtils;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import data.media.writeAsArray.ImageSerializer;
import data.media.writeAsArray.MediaContentSerializer;
import data.media.writeAsArray.MediaSerializer;

public class BenchmarkMain_EishayEncode_WriteAsArray {

    public static void main(String[] args) throws Exception {
        SerializeConfig config = SerializeConfig.getGlobalInstance();
        config.put(MediaContent.class, new MediaContentSerializer());
        config.put(Media.class, new MediaSerializer());
        config.put(Image.class, new ImageSerializer());
        
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
        return JSON.toJSONString(text);
    }
}
