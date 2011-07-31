package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayInt1000Encode extends BenchmarkCase {

    private Object object;

    public ArrayInt1000Encode(){
        super("ArrayInt1000Encode");

        int[] array = new int[1000];
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
