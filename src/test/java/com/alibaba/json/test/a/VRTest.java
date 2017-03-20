package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.vans.VansData;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by wenshao on 05/01/2017.
 */
public class VRTest extends TestCase {
    public void test_vr() throws Exception {
        File file = new File("/Users/wenshao/Downloads/model_p_30687.json");

        file = new File("/Users/wenshao/Downloads/model_p_30687_2.json");
        //

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
            VansData vansData = JSON.parseObject(str, VansData.class);
        }
    }
}
