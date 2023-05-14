package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author gongdewei 2020/5/15
 */
public class SerializeWriterToBytesTest {

    /**
     * Execute toBytes periodically, use tools to analyze JVM memory allocation.
     * For example, Memory Allocation Record of YourKit java profiler
     */
    public static void testLargeStrToBytes() {
        String str = createTestStr();
        for (int i = 0; i < 600; i++) {
            SerializeWriter writer = new SerializeWriter();
            try {
                writer.writeString(str);
                writer.toBytes("UTF-8");
            } finally {
                writer.close();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public static void testLargeStrWriteToEx() throws IOException {
        String str = createTestStr();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length()+2);
        for (int i = 0; i < 600; i++) {
            SerializeWriter writer = new SerializeWriter();
            try {
                writer.writeString(str);
                writer.writeToEx(baos, IOUtils.UTF8);
            } finally {
                writer.close();
                baos.reset();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private static String createTestStr() {
        String tmp = new String(IOUtils.DIGITS);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 400; i++) {
            builder.append(tmp);
        }
        return builder.toString();
    }

    public static void main(String[] args) throws IOException {
        testLargeStrToBytes();
//        testLargeStrWriteToEx();
    }
}
