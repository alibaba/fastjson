package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.IncomingDataPoint;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;

/**
 * Created by wenshao on 04/08/2017.
 */
public class IncomingDataPointBenchmark_file {
    static String json;

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/wenshao/Downloads/datalist");
        json = FileUtils.readFileToString(file);
        for (int i = 0; i < 10; ++i) {
            perf();
        }
    }

    public static void perf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            JSON.parseArray(json, IncomingDataPoint.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("IncomingDataPoint millis : " + millis);
    }
}
