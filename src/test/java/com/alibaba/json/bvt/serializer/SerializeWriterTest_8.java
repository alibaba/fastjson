package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class SerializeWriterTest_8 extends TestCase {
    public void test_BrowserCompatible() throws Exception {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 1024; ++i) {
            buf.append('a');
        }
        buf.append("中国");
        buf.append("\0");
        JSON.toJSONString(buf.toString(), SerializerFeature.BrowserCompatible);
    }
    
    public void test_writer() throws Exception {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 1024; ++i) {
            buf.append('a');
        }
        buf.append("中国");
        buf.append("\0");
        
        StringWriter out = new StringWriter();
        JSON.writeJSONStringTo(buf.toString(), out, SerializerFeature.BrowserCompatible);
    }
}
