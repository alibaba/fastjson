package com.alibaba.json.bvt.issue_3100;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;
import org.junit.Assert;

import java.lang.reflect.Field;

public class Issue3160 extends TestCase {

    public void test_for_issue() throws Exception {
        String str = createLargeBasicStr();
        SerializeWriter writer = new SerializeWriter();
        //写入大于12K的字符串
        writer.writeString(str);
        writer.writeString(str);
        byte[] bytes = writer.toBytes("UTF-8");
        writer.close();

        //检查bytesLocal大小，如果缓存成功应该大于等于输出的bytes长度
        Field bytesBufLocalField = SerializeWriter.class.getDeclaredField("bytesBufLocal");
        bytesBufLocalField.setAccessible(true);
        ThreadLocal<byte[]> bytesBufLocal = (ThreadLocal<byte[]>) bytesBufLocalField.get(null);
        byte[] bytesLocal = bytesBufLocal.get();
        Assert.assertNotNull("bytesLocal is null", bytesLocal);
        Assert.assertTrue("bytesLocal is smaller than expected", bytesLocal.length >= bytes.length);

    }

    private String createLargeBasicStr() {
        String tmp = new String(IOUtils.DIGITS);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 400; i++) {
            builder.append(tmp);
        }
        return builder.toString();
    }
}
