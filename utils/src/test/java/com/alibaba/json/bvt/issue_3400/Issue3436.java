package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;
import org.springframework.core.io.FileSystemResource;

public class Issue3436 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.addMixInAnnotations(FileSystemResource.class, FileSystemResourceMixedIn.class);

        FileSystemResource fileSystemResource = new FileSystemResource("E:\\my-code\\test\\test-fastjson.txt");

        String json = JSON.toJSONString(fileSystemResource);
        assertEquals("{\"path\":\"E:/my-code/test/test-fastjson.txt\"}", json);

        FileSystemResource fsr1 = JSON.parseObject(json, FileSystemResource.class);
        assertEquals(fileSystemResource.getPath(), fsr1.getPath());
        System.out.println("file size after Serialize：" + fileSystemResource.getFile().length());
    }

    @JSONType(asm = false, includes = "path")
    public static class FileSystemResourceMixedIn {
        @JSONCreator
        public FileSystemResourceMixedIn(String path) {

        }
    }
}
