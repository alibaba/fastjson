package com.alibaba.fastjson.validate;

import com.alibaba.fastjson.JSONValidator;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JSONValidateTest_file extends TestCase
{
    public void test_for_file() throws Exception {
        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();

            File file = new File("/Users/wenshao/Downloads/000002_0.json");
            FileInputStream is = new FileInputStream(file);
            JSONValidator validator = JSONValidator.fromUtf8(is);
            assertTrue(validator.validate());
            validator.close();

            // 642 335 796
            System.out.println("millis " + (System.currentTimeMillis() - start));
        }
    }

    public void test_for_fileReader() throws Exception {
        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();

            File file = new File("/Users/wenshao/Downloads/000002_0.json");
            Reader is = new InputStreamReader(new FileInputStream(file), "UTF8");
            JSONValidator validator = JSONValidator.from(is);
            assertTrue(validator.validate());
            validator.close();

            // 642 335 796
            System.out.println("millis " + (System.currentTimeMillis() - start));
        }
    }
}
