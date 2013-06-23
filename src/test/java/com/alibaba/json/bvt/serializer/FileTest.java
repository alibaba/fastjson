package com.alibaba.json.bvt.serializer;

import java.io.File;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;


public class FileTest extends TestCase {

    public void test_file() throws Exception {
        File file = new File("abc.txt");
        
        String text = JSON.toJSONString(file);
        
        Assert.assertEquals(JSON.toJSONString(file.getPath()), text);
                
        File file2 = JSON.parseObject(text, File.class);
        
        Assert.assertEquals(file, file2);
    }
}
