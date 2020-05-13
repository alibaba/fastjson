package com.alibaba.json.test.benchmark.encode;

import java.util.Collections;
import java.util.List;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class ArrayEmptyList1000Encode extends BenchmarkCase {

    private Object object;

    @SuppressWarnings("rawtypes")
    public ArrayEmptyList1000Encode(){
        super("ArrayEmptyList1000Encode");

        List[] array = new List[1000];
        for (int i = 0; i < array.length; ++i) {
            array[i] = Collections.emptyList();
        }
        this.object = array;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
