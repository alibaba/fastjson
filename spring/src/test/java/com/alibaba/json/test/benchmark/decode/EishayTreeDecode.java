package com.alibaba.json.test.benchmark.decode;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class EishayTreeDecode extends BenchmarkCase {

    private final String text;

    public EishayTreeDecode(){
        super("EishayDecode-tree");

        this.text = EishayDecodeBytes.instance.getText();
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decode(text);
    }

}
