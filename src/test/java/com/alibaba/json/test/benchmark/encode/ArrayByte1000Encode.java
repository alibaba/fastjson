package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayByte1000Encode extends BenchmarkCase {

    private Object object;

    public ArrayByte1000Encode(){
        super("ArrayByte1000Encode");

        byte[] array = new byte[1000];
        for (int i = 0; i < array.length; ++i) {
            array[i] = (byte) (i % 100);
        }
        this.object = array;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
