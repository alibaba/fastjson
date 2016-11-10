package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.json.test.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by wenshao on 2016/10/19.
 */
public class Issue859 extends TestCase {
    public void test_for_issue() throws Exception {
        InputStream is = Issue72.class.getClassLoader().getResourceAsStream("issue859.zip");
        GZIPInputStream gzipInputStream = new GZIPInputStream(is);
        String text = org.apache.commons.io.IOUtils.toString(gzipInputStream);
        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < 1; ++i) {
            JSON.parseObject(text);
        }
        // new Gson().fromJson(text, java.util.HashMap.class);
        //new ObjectMapper().readValue(text, java.util.HashMap.class);
        long costMillis = System.currentTimeMillis() - startMillis;
        System.out.println("cost : " + costMillis);
    }
}
