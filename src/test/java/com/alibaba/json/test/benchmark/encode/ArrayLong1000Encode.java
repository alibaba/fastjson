package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayLong1000Encode extends BenchmarkCase {

    private Object object;

    public ArrayLong1000Encode(){
        super("ArrayLong1000Encode");

        long[] array = new long[1000];
        for (int i = 0; i < array.length; ++i) {
            array[i] = i;
        }
        this.object = array;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
