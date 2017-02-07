package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.StringReader;

/**
 * Created by wenshao on 21/01/2017.
 */
public class Issue1001 extends TestCase {
    public void test_for_issue() throws Exception {
        File file = new File("/Users/wenshao/Downloads/issue_1001.json");

        String json = FileUtils.readFileToString(file);

        JSONReader reader = new JSONReader(new StringReader(json));
        reader.readObject();
    }
}
