package com.alibaba.json.test.performance;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;

public class IntegerListEncodePerformanceTest extends TestCase {

    final int             COUNT     = 1000 * 1;
    protected List<Codec> codecList = new ArrayList<Codec>();

    protected void setUp() throws Exception {
        codecList.add(new JacksonCodec());
        codecList.add(new FastjsonCodec());
    }

    public void test_0() throws Exception {
        int len = 1000 * 10;
        Integer[] array = new Integer[len];
        for (int i = 0; i < len; ++i) {
            array[i] = i;
        }

        List<Integer> list = Arrays.asList(array);
        for (Codec codec : codecList) {
            for (int i = 0; i < 5; ++i) {
                encode(list, codec);
            }

            System.out.println();
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
