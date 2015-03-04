package com.alibaba.json.test.codegen;

import java.io.FileWriter;

import junit.framework.TestCase;

import com.alibaba.fastjson.codegen.DeserializerGen;
import data.media.Image;

public class GenImageTest extends TestCase {

    public void test_codegen() throws Exception {
        FileWriter out = new FileWriter("/Users/wenshao/work/git/fastjson/src/test/java/data/media/ImageGenDecoder.java");

        DeserializerGen generator = new DeserializerGen(Image.class, out);

        generator.gen();
        
        out.flush();
        out.close();

        // System.out.println(out.toString());
    }
}
