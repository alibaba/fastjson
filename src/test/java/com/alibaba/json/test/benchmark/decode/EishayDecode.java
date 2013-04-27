package com.alibaba.json.test.benchmark.decode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

import data.media.MediaContent;

public class EishayDecode extends BenchmarkCase {

    private final String text;

    public EishayDecode(){
        super("EishayDecode");

        this.text = EishayDecodeBytes.instance.getText();
        System.out.println(text);
        
        //JavaBeanMapping.getGlobalInstance().putDeserializer(Image.class, new ImageDeserializer());
        //JavaBeanMapping.getGlobalInstance().putDeserializer(Media.class, new MediaDeserializer());
    }

    @Override
    public void execute(Codec codec) throws Exception {
        MediaContent content = codec.decodeObject(text, MediaContent.class);
        if (content == null) {
            throw new Exception();
        }
    }

}
