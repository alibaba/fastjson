package com.alibaba.json.test.benchmark.decode;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

/**
 * Created by wenshao on 16/4/4.
 */
public class TradeObjectParse extends BenchmarkCase {

    private String text;

    public TradeObjectParse(){
        super("TradeObjectParse");

        // JavaBeanMapping.getGlobalInstance().putDeserializer(Image.class, new ImageDeserializer());
        // JavaBeanMapping.getGlobalInstance().putDeserializer(Media.class, new MediaDeserializer());
    }

    public void init(Codec codec) throws Exception {
        InputStream is = TradeObjectParse.class.getClassLoader().getResourceAsStream("json/trade.json");
        Reader reader = new InputStreamReader(is, "UTF-8");
        char[] chars = new char[1024];

        StringBuilder out = new StringBuilder();
        for (;;) {
            int len = reader.read(chars);
            if (len <= 0) {
                break;
            }
            out.append(chars, 0, len);
        }
        reader.close();
        text = out.toString();
        System.out.println("tradeJsonObject " + text.substring(100));
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decode(text);
    }

}