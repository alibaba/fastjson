package com.alibaba.json.bvt.bug;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.util.IOUtils;

public class Issue72 extends TestCase {

    public void test_for_issue() throws Exception {
        InputStream is = Issue72.class.getClassLoader().getResourceAsStream("issue72.json");
        JSONReader reader = null;
        try {
            byte[] rowBatchBytes = null;
            byte[] fileBatchBytes = null;
            reader = new JSONReader(new InputStreamReader(is));
            reader.startArray();
            while (reader.hasNext()) {
                if (rowBatchBytes == null) {
                    rowBatchBytes = reader.readObject(byte[].class);
                } else if (fileBatchBytes == null) {
                    fileBatchBytes = reader.readObject(byte[].class);
                } else {
                    throw new Exception("archive data json parse failed!");
                }

            }
            reader.endArray();
        } finally {
            IOUtils.close(reader);
        }
    }
}
