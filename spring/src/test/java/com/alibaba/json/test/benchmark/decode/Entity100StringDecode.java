package com.alibaba.json.test.benchmark.decode;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.benchmark.entity.Entity100String;
import com.alibaba.json.test.codec.Codec;

public class Entity100StringDecode extends BenchmarkCase {

    private String text;

    public Entity100StringDecode(){
        super("Entity100StringDecode");

        this.text = JSON.toJSONString(new Entity100String());
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decodeObject(text, Entity100String.class);
    }
}
