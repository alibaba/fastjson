package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

public class IgoreGetterTest extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = new VO();
        assertEquals("{}", JSON.toJSONString(vo));
    }

    public static class VO {
        public InputStream getInputStream() {
            throw  new UnsupportedOperationException();
        }

        public Reader getReader() {
            throw  new UnsupportedOperationException();
        }
    }
}
