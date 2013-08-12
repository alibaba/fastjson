package com.alibaba.json.bvt.bug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;

public class Bug12 extends TestCase {

    public void test_0() throws Exception {
        File folder = new File("D:\\wenshao\\downloads\\json_src");
        for (File file : folder.listFiles()) {
            File outfile = new File(folder, file.getName() + ".json");

            JSONReader reader = new JSONReader(new InputStreamReader(new FileInputStream(file)));
            JSONWriter writer = new JSONWriter(new FileWriter(outfile));
            for (int i = 0; i < 40; ++i) {
                Object obj = reader.readObject();
                
                if (obj == null) {
                    break;
                }
                writer.writeObject(obj);
            }

            reader.close();
            writer.close();
        }
       
    }
}
