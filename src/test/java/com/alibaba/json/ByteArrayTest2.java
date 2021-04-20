/*
 * @Author: your name
 * @Date: 2021-04-10 23:10:01
 * @LastEditTime: 2021-04-20 14:14:32
 * @LastEditors: your name
 * @Description: In User Settings Edit
 * @FilePath: \fastjson\src\test\java\com\alibaba\json\ByteArrayTest2.java
 */
package com.alibaba.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import static com.alibaba.fastjson.parser.ParserConfig.AUTO_SUPPORT;


public class ByteArrayTest2 extends TestCase {
    public void setUp() {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    public void tearDown() {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(AUTO_SUPPORT);

    }

    public static class CertFile {
        public String name;
        public byte[] data;
    }

    public void test_0() throws Exception {

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
        CertFile convertFile = (CertFile) JSON.parse(data, 0, data.length, charset.newDecoder(), Feature.AllowArbitraryCommas,
                Feature.IgnoreNotMatch, Feature.SortFeidFastMatch, Feature.DisableCircularReferenceDetect,
                Feature.AutoCloseSource);

        Assert.assertEquals(file.name, convertFile.name);
        Assert.assertArrayEquals(file.data, convertFile.data);
    }
}
