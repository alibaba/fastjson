package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.json.test.vans.VansData;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by wenshao on 05/01/2017.
 */
public class VRTest_parse extends TestCase {
    public void test_vr() throws Exception {
        JSON.DEFAULT_PARSER_FEATURE &= ~Feature.UseBigDecimal.mask;

        File file = new File("/Users/wenshao/Downloads/model_p_30687.json");

        String str = FileUtils.readFileToString(file);

        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();

            perf(str);
            long millis = System.currentTimeMillis() - start;
            System.out.println("millis : " + millis);
        }
    }

    private void perf(String str) {
        for (int i = 0; i < 10; ++i) {
            JSON.parseObject(str);
        }
    }
}
