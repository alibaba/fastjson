package com.alibaba.json.bvt.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest__entity_enum extends TestCase {

    public void test_scanInt() throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        for (int i = 0; i < 10; ++i) {
            if (i != 0) {
                buf.append(',');
            }
            //1000000000000
            //
            Type type;
            if (i % 3 == 0) {
                type = Type.A;
            } else if (i % 3 == 1) {
                type = Type.AA;
            } else {
                type = Type.AAA;
            }
            buf.append("{\"id\":\"" + type.name() + "\"}");
            
        }
        buf.append(']');

        Reader reader = new StringReader(buf.toString());

        JSONReaderScanner scanner = new JSONReaderScanner(reader);

        DefaultJSONParser parser = new DefaultJSONParser(scanner);
        List<VO> array = parser.parseArray(VO.class);
        for (int i = 0; i < array.size(); ++i) {
            Type type;
            if (i % 3 == 0) {
                type = Type.A;
            } else if (i % 3 == 1) {
                type = Type.AA;
            } else {
                type = Type.AAA;
            }
            
            Assert.assertEquals(type, array.get(i).getId()); 
        }
    }

    public static class VO {

        private Type id;

        public Type getId() {
            return id;
        }

        public void setId(Type id) {
            this.id = id;
        }
    }
    
    public static enum Type {
        A, AA, AAA
    }
}
