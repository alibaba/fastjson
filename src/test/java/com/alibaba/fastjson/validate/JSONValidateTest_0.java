package com.alibaba.fastjson.validate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;

public class JSONValidateTest_0 extends TestCase {
    public void test_validate() throws Exception {
        String json = JSON.toJSONString(EishayDecodeBytes.instance.getContent());
        JSONValidator validator = JSONValidator.from(json);
        assertTrue(validator.validate());
    }

    public void test_validate_benchmark() throws Exception {
        String json = JSON.toJSONString(EishayDecodeBytes.instance.getContent());

        for (int n = 0; n < 10; ++n) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000 * 1000 * 1; ++i) {
                JSONValidator validator = JSONValidator.from(json);
                validator.validate(); // 518
            }
            System.out.println("millis : " + (System.currentTimeMillis() - start));
        }
    }

    public void test_validate_utf8() throws Exception {
        byte[] json = JSON.toJSONBytes(EishayDecodeBytes.instance.getContent());

        JSONValidator validator = JSONValidator.fromUtf8(json);
        assertTrue(validator.validate());
    }

    public void test_validate_utf8_stream() throws Exception {
        byte[] json = JSON.toJSONBytes(EishayDecodeBytes.instance.getContent());

        JSONValidator validator = JSONValidator.fromUtf8(new ByteArrayInputStream(json));
        assertTrue(validator.validate());
        validator.close();
    }

    public void test_validate_utf8_benchmark() throws Exception {
        byte[] json = JSON.toJSONBytes(EishayDecodeBytes.instance.getContent());

        for (int n = 0; n < 5; ++n) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000 * 1000 * 1; ++i) {
                JSONValidator validator = JSONValidator.fromUtf8(json);
                validator.validate();
            }
            System.out.println("millis : " + (System.currentTimeMillis() - start));
        }
    }
}
