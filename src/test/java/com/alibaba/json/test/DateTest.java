package com.alibaba.json.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.GsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;
import com.alibaba.json.test.codec.SimpleJsonCodec;

public class DateTest extends TestCase {

    public void test_0() throws Exception {
        List<Codec> decoders = new ArrayList<Codec>();
        decoders.add(new FastjsonCodec());
        decoders.add(new JacksonCodec());
        decoders.add(new SimpleJsonCodec());
        // decoders.add(new JsonLibDecoderImpl());
        decoders.add(new GsonCodec());

        long time = System.currentTimeMillis();
        for (Codec codec : decoders) {
            String text = codec.encode(new java.sql.Date(time));
            System.out.println(codec.getName() + " : " + text);
            // codec.decodeObject(text, java.sql.Date.class);
        }
    }
}
