package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayString1000Encode extends BenchmarkCase {

    private Object object;

    public ArrayString1000Encode(){
        super("ArrayString1000Encode");

        String[] array = new String[1000];
        for (int i = 0; i < array.length; ++i) {
            array[i] = Integer.toHexString(i * 1000);
        }
        this.object = array;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
