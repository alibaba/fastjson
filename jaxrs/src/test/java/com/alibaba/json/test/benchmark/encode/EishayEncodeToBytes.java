package com.alibaba.json.test.benchmark.encode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class EishayEncodeToBytes extends BenchmarkCase {

    public EishayEncodeToBytes(){
        super("EishayEncodeToBytes");

    }

    @Override
    public void execute(Codec codec) throws Exception {
        byte[] text = codec.encodeToBytes(EishayEncode.mediaContent);
        if (text == null) {
            throw new Exception();
        }
    }


}
