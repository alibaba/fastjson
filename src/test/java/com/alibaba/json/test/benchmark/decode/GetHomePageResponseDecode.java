package com.alibaba.json.test.benchmark.decode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.json.bvt.taobao.GetHomePageResponse;
import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class GetHomePageResponseDecode extends BenchmarkCase {
    private String text;
    
    public GetHomePageResponseDecode(){
        super("GetHomePageResponse-Decode");
    }
    
    public void init(Codec codec) throws Exception {
        InputStream is = GetHomePageResponseDecode.class.getClassLoader().getResourceAsStream("json/taobao/homepage.json");
        text = toString(is);
        is.close();
    }

    @Override
    public void execute(Codec codec) throws Exception {
        GetHomePageResponse resp = JSON.parseObject(text, GetHomePageResponse.class);
        if (resp.data == null) {
            throw new IllegalStateException();
        }
    }

    public static String toString(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return readAll(reader);
    }
    
    public static String readAll(Reader reader) {
        StringBuilder buf = new StringBuilder();
        
        try {
            char[] chars = new char[2048];
            for (;;) {
                int len = reader.read(chars, 0, chars.length);
                if (len < 0) {
                    break;
                }
                buf.append(chars, 0, len);
            }
        } catch(Exception ex) {
            throw new JSONException("read string from reader error", ex);
        }
        
        return buf.toString();
    }
}
