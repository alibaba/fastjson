package com.alibaba.json.test.benchmark.decode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class IntArray1000Decode extends BenchmarkCase {

    private String text;

    public IntArray1000Decode(){
        super("IntArray1000Decode");

        StringBuilder buf = new StringBuilder();

        buf.append('[');
        for (int i = 0; i < 1000; ++i) {
            if (i != 0) {
                buf.append(",");
            }
            buf.append(i);
        }
        buf.append(']');
        this.text = buf.toString();
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decode(text);
    }
}
