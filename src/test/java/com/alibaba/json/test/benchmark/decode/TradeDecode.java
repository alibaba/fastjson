package com.alibaba.json.test.benchmark.decode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class TradeDecode extends BenchmarkCase {

    private static String text;

    public TradeDecode(){
        super("TradeDecode");

        // JavaBeanMapping.getGlobalInstance().putDeserializer(Image.class, new ImageDeserializer());
        // JavaBeanMapping.getGlobalInstance().putDeserializer(Media.class, new MediaDeserializer());
    }

    public void init(Codec codec) throws Exception {
        if (text != null) {
            return;
        }
        text = TradeParse.readFromResource();
        
//        JSONObject object = JSON.parseObject(text);
//        String prettyText = JSON.toJSONString(object, SerializerFeature.PrettyFormat);
//        System.out.println(prettyText);
        System.out.println("tradeJsonObject " + text.substring(100));
    }

    @Override
    public void execute(Codec codec) throws Exception {
        
    }

}
