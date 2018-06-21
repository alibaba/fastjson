package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class FactoryTest extends TestCase {

    public void test_factory() throws Exception {
        VO vo = JSON.parseObject("{\"b\":true,\"i\":33,\"l\":34,\"f\":45.}", VO.class);
        Assert.assertEquals(true, vo.isB());
        Assert.assertEquals(33, vo.getI());
        Assert.assertEquals(34L, vo.getL());
        Assert.assertTrue(45f == vo.getF());
        JSON.parseObject("{\"b\":1,\"i\":33,\"l\":34,\"f\":45.}", VO.class);
    }
    
    public void test_factory1() throws Exception {
        V1 vo = JSON.parseObject("{\"b\":true,\"i\":33,\"l\":34,\"f\":45.}", V1.class);
        Assert.assertEquals(true, vo.isB());
        Assert.assertEquals(33, vo.getI());
        Assert.assertEquals(34L, vo.getL());
        Assert.assertTrue(45f == vo.getF());
        JSON.parseObject("{\"b\":1,\"i\":33,\"l\":34,\"f\":45.}", V1.class);
        
        // JSON.parseObject("{\"b\":true,\"i\":33,\"l\":34,\"f\":45.}").toJavaObject(V1.class);
    }

    public static class VO {

        private final boolean b;
        private final int     i;
        private final long    l;
        private final float   f;

        @JSONCreator
        public VO(@JSONField(name = "b") boolean b, @JSONField(name = "i") int i, @JSONField(name = "l") long l,
                  @JSONField(name = "f") float f){
            super();
            this.b = b;
            this.i = i;
            this.l = l;
            this.f = f;
        }

        public float getF() {
            return f;
        }

        public boolean isB() {
            return b;
        }

        public int getI() {
            return i;
        }

        public long getL() {
            return l;
        }

    }

    public static class V1 {

        private boolean b;
        private int     i;
        private long    l;
        private float   f;
        
        private V1(boolean b) {
            this.b = b;
        }

        @JSONCreator
        public static V1 create(@JSONField(name = "b") boolean b, @JSONField(name = "i") int i,
                                @JSONField(name = "l") long l, @JSONField(name = "f") float f) {
            V1 v = new V1(b);
            v.i = i;
            v.l = l;
            v.f = f;

            return v;
        }

        public float getF() {
            return f;
        }

        public boolean isB() {
            return b;
        }

        public int getI() {
            return i;
        }

        public long getL() {
            return l;
        }

    }
}
