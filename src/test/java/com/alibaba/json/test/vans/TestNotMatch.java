package com.alibaba.json.test.vans;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 20/02/2017.
 */
public class TestNotMatch extends TestCase {
    public void test_0() throws Exception {
        for (int i = 0; i < 10;++i) {
            long startMillis = System.currentTimeMillis();
            perf("{\"name\":\"abc\",\"id\":123,\"desc\":\"ddd\"}");
            //perf("{\"desc\":\"ddd\",\"id\":123,\"name\":\"abc\"}");
            long millis = System.currentTimeMillis() - startMillis;
            System.out.println("millis : " + millis);

        }
    }

    public static void perf(String text) throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {
            Model model = JSON.parseObject(text, Model.class);
        }
    }

    public static class Model {
        public int id;
        public String name;
        public String desc;
    }
}
