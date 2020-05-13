package com.alibaba.json.test.performance;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;

public class IntArrayEncodePerformanceTest extends TestCase {

    final int             COUNT     = 10;
    protected List<Codec> codecList = new ArrayList<Codec>();

    protected void setUp() throws Exception {
        codecList.add(new JacksonCodec());
        codecList.add(new FastjsonCodec());
    }

    public void test_0() throws Exception {
        int len = 1000 * 1000;
        int[] array = new int[len];
        for (int i = 0; i < len; ++i) {
            array[i] = i;
        }

        for (Codec codec : codecList) {
            for (int i = 0; i < COUNT; ++i) {
                encode(array, codec);
            }
        }
    }

    private void encode(Object object, Codec decoder) throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            decoder.encode(object);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(decoder.getName() + " : \t" + NumberFormat.getInstance().format(nano));
    }
}
