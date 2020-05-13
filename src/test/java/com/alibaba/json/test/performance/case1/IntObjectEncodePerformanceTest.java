package com.alibaba.json.test.performance.case1;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.SimpleJsonCodec;

public class IntObjectEncodePerformanceTest extends TestCase {

    private String    text;
    private final int COUNT = 1000 * 100;
    private Object    object;

    protected void setUp() throws Exception {
        String resource = "json/int_array_1000.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        text = IOUtils.toString(is);
        is.close();

        // text = "{\"age\":9223372036854775807}";
        // object = JSON.parseObject(text, Long_100_Entity.class);
        // object = JSON.parseArray(text);
        {
            ArrayList<Long> array = new ArrayList<Long>();
            for (long i = 0; i < 1000; ++i) {
                array.add(i);
            }
            object = array;
        }
    }

    public void test_encodeObject() throws Exception {
        List<Codec> decoders = new ArrayList<Codec>();
        // decoders.add(new FastjsonDecoderImpl());
        // decoders.add(new JacksonDecoderImpl());
        decoders.add(new SimpleJsonCodec());
        // decoders.add(new JsonLibDecoderImpl());

        for (int i = 0; i < 10; ++i) {
            for (Codec decoder : decoders) {
                encode(object, decoder);
            }
            // System.out.println();
        }
        // System.out.println();
        // System.out.println(text);
    }

    public void encode(Object object, Codec decoder) throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            decoder.encode(object);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(decoder.getName() + " : \t" + NumberFormat.getInstance().format(nano));
    }
}
