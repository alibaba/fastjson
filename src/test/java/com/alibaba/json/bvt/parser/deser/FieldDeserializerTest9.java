package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;


public class FieldDeserializerTest9 extends TestCase {
    public void test_0 () throws Exception {
        assertTrue(JSON.parseObject("{\"id\":true\t}", VO.class).id);
        assertTrue(JSON.parseObject("{\"id\":true\t}\n\t", VO.class).id);
        assertTrue(JSON.parseObject("{\"id\":true }", V1.class).id);
        assertTrue(JSON.parseObject("{\"id\":true }\n\t", V1.class).id);
        assertTrue(JSON.parseObject("{\"id\":true\n}", V1.class).id);
    }

    public void test_1 () throws Exception {
        assertFalse(JSON.parseObject("{\"id\":false\t}", VO.class).id);
        assertFalse(JSON.parseObject("{\"id\":false\t}\n\t", VO.class).id);
        assertFalse(JSON.parseObject("{\"id\":false }", V1.class).id);
        assertFalse(JSON.parseObject("{\"id\":false }\n\t", V1.class).id);
        assertFalse(JSON.parseObject("{\"id\":false\n}", V1.class).id);
    }
    
    public static class VO {
        public boolean id;
    }
    
    private static class V1 {
        public boolean id;
    }
}
