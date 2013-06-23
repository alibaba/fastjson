package com.alibaba.json.bvt.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest__entity_double extends TestCase {

    public void test_scanFloat() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        for (int i = 0; i < 1024; ++i) {
            if (i != 0) {
                buf.append(',');
            }
            buf.append("{\"id\":" + i + ".0}");
        }
        buf.append(']');

        Reader reader = new StringReader(buf.toString());

        JSONReaderScanner scanner = new JSONReaderScanner(reader);

        DefaultJSONParser parser = new DefaultJSONParser(scanner);
        List<VO> array = parser.parseArray(VO.class);
        for (int i = 0; i < array.size(); ++i) {
            Assert.assertTrue((double) i == array.get(i).getId());
        }
        parser.close();
    }

    public static class VO {

        private double id;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

    }
}
