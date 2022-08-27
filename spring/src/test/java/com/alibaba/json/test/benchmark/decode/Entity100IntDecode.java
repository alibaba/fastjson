package com.alibaba.json.test.benchmark.decode;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.benchmark.entity.Entity100Int;
import com.alibaba.json.test.codec.Codec;

public class Entity100IntDecode extends BenchmarkCase {

    private String text;

    public Entity100IntDecode(){
        super("StringArray1000Decode");

        this.text = JSON.toJSONString(new Entity100Int());
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decodeObject(text, Entity100Int.class);
    }
}
