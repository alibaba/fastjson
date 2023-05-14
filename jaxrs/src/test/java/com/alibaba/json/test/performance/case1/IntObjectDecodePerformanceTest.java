package com.alibaba.json.test.performance.case1;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;
import com.alibaba.json.test.entity.case1.LongObject_100_Entity;

public class IntObjectDecodePerformanceTest extends TestCase {

    private String    text;
    private final int COUNT = 1000 * 100;

    protected void setUp() throws Exception {
        String resource = "json/object_f_int_1000.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        text = IOUtils.toString(is);
        is.close();
    }

    public void test_decodeObject() throws Exception {
        List<Codec> decoders = new ArrayList<Codec>();
        decoders.add(new FastjsonCodec());
        decoders.add(new JacksonCodec());

        for (int i = 0; i < 10; ++i) {
            for (Codec decoder : decoders) {
                decodeToJavaBean(text, decoder);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println(text);
    }

    public void decodeToJavaBean(String text, Codec decoder) throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            decoder.decodeObject(text, LongObject_100_Entity.class);
            // decoder.decode(text);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(decoder.getName() + " : \t" + NumberFormat.getInstance().format(nano));
    }
}
