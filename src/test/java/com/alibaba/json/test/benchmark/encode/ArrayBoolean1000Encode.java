package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayBoolean1000Encode extends BenchmarkCase {

    private Object object;

    public ArrayBoolean1000Encode(){
        super("BooleanArray1000Encode");

        boolean[] array = new boolean[1000];
        for (int i = 0; i < array.length; ++i) {
            array[i] = (i % 2 == 0);
        }
        this.object = array;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
