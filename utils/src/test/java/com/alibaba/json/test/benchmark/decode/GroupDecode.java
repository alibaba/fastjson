package com.alibaba.json.test.benchmark.decode;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

public class GroupDecode extends BenchmarkCase {

    private String text;

    public GroupDecode(){
        super("GroupDecode");

        try {
            String resource = "json/group.json";
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            text = IOUtils.toString(is);
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void execute(Codec codec) throws Exception {
        for (int i = 0; i < 10; ++i) {
            codec.decodeObject(text);
        }
    }
}
