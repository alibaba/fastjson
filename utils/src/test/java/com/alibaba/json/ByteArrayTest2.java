package com.alibaba.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


public class ByteArrayTest2  extends TestCase {

    public static class CertFile {
        public String name;
        public byte[] data;
    }

    public void test_0() throws Exception {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        CertFile file = new CertFile();
        file.name = "testname";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2048; i++) {
            sb.append("1");
        }
        file.data = sb.toString().getBytes();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JSONWriter writer = new JSONWriter(new OutputStreamWriter(bos));
        writer.config(SerializerFeature.WriteClassName, true);
        writer.writeObject(file);
        writer.flush();

        System.out.println(bos);

        byte[] data = bos.toByteArray();
        Charset charset = Charset.forName("UTF-8");
        CertFile convertFile =  (CertFile)JSON.parse(data, 0, data.length, charset.newDecoder(), Feature.AllowArbitraryCommas,
                Feature.IgnoreNotMatch, Feature.SortFeidFastMatch, Feature.DisableCircularReferenceDetect,
                Feature.AutoCloseSource);

        Assert.assertEquals(file.name, convertFile.name);
        Assert.assertArrayEquals(file.data, convertFile.data);
    }
}
