package com.alibaba.json.test.benchmark.decode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.benchmark.encode.Eishay2Encode;
import com.alibaba.json.test.codec.Codec;

import data.media.MediaContent;

public class Eishay2Decode extends BenchmarkCase {

    private String text;

    public Eishay2Decode(){
        super("Eishay2Decode");

        // JavaBeanMapping.getGlobalInstance().putDeserializer(Image.class, new ImageDeserializer());
        // JavaBeanMapping.getGlobalInstance().putDeserializer(Media.class, new MediaDeserializer());
    }

    public void init(Codec codec) throws Exception {
        this.text = codec.encode(Eishay2Encode.mediaContent);
        System.out.println(text);
    }

    @Override
    public void execute(Codec codec) throws Exception {
        MediaContent content = codec.decodeObject(text, MediaContent.class);
        if (content == null) {
            throw new Exception();
        }
    }

}
