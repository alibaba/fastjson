package com.alibaba.json;

import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

class CertFile {
    public String name;
    public byte[] data;
}

public class ByteArrayTest {

    public static void main(String[] args) {
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
