package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.IncomingDataPoint_double;
import com.alibaba.json.bvtVO.IncomingDataPoint_ext_double;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by wenshao on 04/08/2017.
 */
public class IncomingDataPointBenchmark_file_ext_double {
    static String json;

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/wenshao/Downloads/datalist_double");
        json = FileUtils.readFileToString(file);
        for (int i = 0; i < 10; ++i) {
            perf();
        }
    }

    public static void perf() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            JSON.parseArray(json, IncomingDataPoint_ext_double.class);
        }
        long millis = System.currentTimeMillis() - start;
        System.out.println("IncomingDataPoint_double millis : " + millis);
    }
}
