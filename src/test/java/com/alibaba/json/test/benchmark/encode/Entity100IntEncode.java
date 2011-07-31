package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.benchmark.entity.Entity100Int;
import com.alibaba.json.test.codec.Codec;

public class Entity100IntEncode extends BenchmarkCase {

    private Object object;

    public Entity100IntEncode(){
        super("Entity100IntEncode");

        Entity100Int entity = new Entity100Int();

        this.object = entity;
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.encode(object);
    }
}
