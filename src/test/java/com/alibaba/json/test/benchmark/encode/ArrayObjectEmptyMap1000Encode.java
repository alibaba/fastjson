package com.alibaba.json.test.benchmark.encode;

import java.util.Collections;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayObjectEmptyMap1000Encode extends BenchmarkCase {

    private Object object;

    public ArrayObjectEmptyMap1000Encode(){
        super("ArrayObjectEmptyMap1000Encode");

        Object[] array = new Object[1000];
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
