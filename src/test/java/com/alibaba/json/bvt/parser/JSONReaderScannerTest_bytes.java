package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;

public class JSONReaderScannerTest_bytes extends TestCase {

    public void test_e() throws Exception {
        VO vo = new VO();
        vo.setValue("ABC".getBytes("UTF-8"));
        
        String text = JSON.toJSONString(vo);
        
        JSONReader reader = new JSONReader(new StringReader(text));
        VO vo2 = reader.readObject(VO.class);
        Assert.assertEquals("ABC", new String(vo2.getValue()));
        reader.close();
    }

    public static class VO {

        private byte[] value;

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }

    }
}
