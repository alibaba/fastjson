package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

public class MaximumLevelTest extends TestCase {
    public void test_for_maximum() throws Exception {
        int[] chars = new int[] {0x5b, 0x7b};

        for (int ch : chars) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 1000; ++i) {
                sb.append((char) ch);
            }

            Exception error = null;
            try {
                JSON.parseObject(sb.toString());
            } catch (JSONException ex) {
                error = ex;
            }

            assertNotNull(error);
        }

    }
}
