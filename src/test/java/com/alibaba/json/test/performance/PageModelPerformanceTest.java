package com.alibaba.json.test.performance;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;
import com.alibaba.json.test.entity.pagemodel.PageInstance;

public class PageModelPerformanceTest extends TestCase {

    private String    text;
    private final int COUNT = 1000;

    protected void setUp() throws Exception {
        text = "{\"badboy\":true,\"description\":\"神棍敌人姐\",\"name\":\"校长\",\"age\":3,\"birthdate\":1293278091773,\"salary\":123456789.0123}";

        String resource = "json/page_model_cached.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        text = IOUtils.toString(is);
        is.close();
    }

    public void test_decodeObject() throws Exception {
        List<Codec> decoders = new ArrayList<Codec>();
        decoders.add(new JacksonCodec());
        decoders.add(new FastjsonCodec());

        for (int i = 0; i < 20; ++i) {
            for (Codec decoder : decoders) {
                decodeToJavaBean(text, decoder);
            }
            System.out.println();
        }
        System.out.println();
        // System.out.println(text);
    }

    public void decodeToJavaBean(String text, Codec decoder) throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            decoder.decodeObject(text, PageInstance.class);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(decoder.getName() + " : \t" + NumberFormat.getInstance().format(nano));
    }
}
