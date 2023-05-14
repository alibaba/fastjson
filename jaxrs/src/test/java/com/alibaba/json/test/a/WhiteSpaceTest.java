package com.alibaba.json.test.a;

import com.alibaba.fastjson.util.IOUtils;

public class WhiteSpaceTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; ++i) {
            perf();
        }
        //ch < IOUtils.whitespaceFlags.length && IOUtils.whitespaceFlags[ch]
    }

    protected static void perf() {
        int count = 0;

        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 200; ++i) {
            for (char ch = 0; ch < 10000; ++ch) {
                
//                boolean whitespace = c < 33 && (4294981376L & (1L << c)) != 0;
//                boolean whitespace = c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '\f' || c == '\b';
                  boolean whitespace = ch <= ' ' && (ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t' || ch == '\f' || ch == '\b');
                if (whitespace) {
                    count++;
                }
//                if (ch < IOUtils.whitespaceFlags.length && IOUtils.whitespaceFlags[ch]) {
//                    count++;
//                }
//                if (c <= ' ' && (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '\f' || c == '\b')) {
//                    count++;
//                }
            }
        }
        
        long endMillis = System.currentTimeMillis();
        long millis = endMillis - startMillis;
        System.out.println("millis : " + millis + ", count " + count);
    }
}
