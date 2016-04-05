package com.alibaba.json.test.benchmark.decode;

import java.util.List;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

import data.xuelu.MyTestJson;

public class XueluDecode extends BenchmarkCase {
    private static String text = MyTestJson.createJson();

    public XueluDecode(){
        super("XueluDecode");
    }
    
    public void init(Codec codec) throws Exception {
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decodeObject(text, new TypeReference<List<MyTestJson>>() {}.getType());
    }

}
