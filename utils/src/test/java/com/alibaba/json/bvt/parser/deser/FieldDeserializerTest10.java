package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;


public class FieldDeserializerTest10 extends TestCase {
    public void test_0 () throws Exception {
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"id\":\"Big\"\t}", VO.class).id);
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"id\":\"Big\"\t}\n\t", VO.class).id);
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"id\":\"Big\" }", V1.class).id);
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"id\":\"Big\" }\n", V1.class).id);
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"id\":\"Big\" }\n\t", V1.class).id);
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"id\":\"Big\"\n}", V1.class).id);
    }
    
    public static class VO {
        public Type id;
    }
    
    private static class V1 {
        public Type id;
    }

    public static enum Type {
        Big, Small
    }
}
