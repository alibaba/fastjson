package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvtVO.mogujie.BindQueryRespDTO;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;

/**
 * Created by wenshao on 16/03/2017.
 */
public class Mogujie_01 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("{}", Model.class);


    }
    public static class Model {
        public int f0;
        public int f1;
        public int f2;
        public int f3;
        public int f4;
        public int f5;
        public int f6;
        public int f7;
        public int f8;
        public int f9;
        public int f10;
        public int f11;
        public int f12;
        public int f13;
        public int f14;
        public int f15;
        public int f16;
        public int f17;
        public int f18;
        public int f19;
        public int f20;
        public int f21;
        public int f22;
        public int f23;
        public int f24;
        public int f25;
        public int f26;
        public int f27;
        public int f28;
        public int f29;
        public int f30;
        public int f31;
    }

}
