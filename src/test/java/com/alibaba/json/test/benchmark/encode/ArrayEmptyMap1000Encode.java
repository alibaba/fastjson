package com.alibaba.json.test.benchmark.encode;

import java.util.Collections;
import java.util.Map;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayEmptyMap1000Encode extends BenchmarkCase {

    private Object object;

    @SuppressWarnings("rawtypes")
    public ArrayEmptyMap1000Encode(){
        super("ArrayEmptyMap1000Encode");

        Map[] array = new Map[1000];
        for (int i = 0; i < array.length; ++i) {
            array[i] = Collections.emptyMap();
        }
        this.object = array;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
