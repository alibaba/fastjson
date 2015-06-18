package com.alibaba.json.bvt.parser;

import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest__map_string extends TestCase {

    public void test_scanInt() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        for (int i = 0; i < 10; ++i) {
            if (i != 0) {
                buf.append(',');
            }
            // 1000000000000
            //
            buf.append("{\"id\":\"" + i + "\"}");

        }
        buf.append(']');

        Reader reader = new StringReader(buf.toString());

        JSONReaderScanner scanner = new JSONReaderScanner(reader);

        DefaultJSONParser parser = new DefaultJSONParser(scanner);
        JSONArray array = (JSONArray) parser.parse();
        for (int i = 0; i < array.size(); ++i) {
            Assert.assertEquals(Integer.toString(i), array.getJSONObject(i).get("id"));
        }
    }

}
