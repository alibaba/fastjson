package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Bug_for_xiedun extends TestCase {
    public void test_for_issue() throws Exception {
//        File file = new File("/Users/wenshao/Downloads/json.txt");
//
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        char[] buf = new char[20480];
//        int readed = reader.read(buf);
//        String content = new String(buf);
//
//        JSON.parse(content);
    }
}
