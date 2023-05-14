package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.io.*;

/**
 * Created by wenshao on 04/02/2017.
 */
public class CompilerTest extends TestCase {
    public void test_for_compiler() throws Exception {
        byte[] bytes;
        {
            Model model = new Model();
            model.id = 123;

            bytes = toBytes(model);
        }

        perf(bytes);
        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();
            perf(bytes);
            long millis = System.currentTimeMillis() - start;
            System.out.println("millis : " + millis);
        }
    }

    private void perf(byte[] bytes) throws IOException, ClassNotFoundException {
        for (int i = 0; i < 1000; ++i) {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Model model = (Model) in.readObject();
            assertEquals(123, model.id);
        }
    }

    private byte[] toBytes(Model model) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);

        out.writeObject(model);
        out.flush();
        byte[] bytes = byteOut.toByteArray();
        out.close();
        return bytes;
    }

    public static class Model implements Serializable {
        public int id;
    }
}
