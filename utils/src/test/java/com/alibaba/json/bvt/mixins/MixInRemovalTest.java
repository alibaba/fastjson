package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class MixInRemovalTest extends TestCase {
    static class BaseClass {
        public int a;
        public int b;

        public  BaseClass() {

        }

        public BaseClass(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    class MixIn1 {
        @JSONField(name = "apple")
        public int a;
        @JSONField(name = "banana")
        public int b;
    }

    class MixIn2 {
        @JSONField(name = "ariston")
        public int a;
        @JSONField(name = "brilliant")
        public int b;
    }

    public void test_mixInRemoval_serialize() throws Exception {
        BaseClass base = new BaseClass(1, 2);
        Assert.assertEquals("{\"a\":1,\"b\":2}", JSON.toJSONString(base));

        JSON.addMixInAnnotations(BaseClass.class, MixIn1.class);
        Assert.assertEquals("{\"apple\":1,\"banana\":2}", JSON.toJSONString(base));
        JSON.removeMixInAnnotations(BaseClass.class);

        JSON.addMixInAnnotations(BaseClass.class, MixIn2.class);
        Assert.assertEquals("{\"ariston\":1,\"brilliant\":2}", JSON.toJSONString(base));
        JSON.removeMixInAnnotations(BaseClass.class);

        Assert.assertEquals("{\"a\":1,\"b\":2}", JSON.toJSONString(base));
    }

    public void test_mixInRemoval_deserialize() throws Exception {
        BaseClass base = JSON.parseObject("{\"a\":1,\"b\":2}", BaseClass.class);
        Assert.assertEquals(1, base.a);
        Assert.assertEquals(2, base.b);

        JSON.addMixInAnnotations(BaseClass.class, MixIn1.class);
        BaseClass base2 = JSON.parseObject("{\"apple\":3,\"banana\":4}", BaseClass.class);
        Assert.assertEquals(3, base2.a);
        Assert.assertEquals(4, base2.b);
        JSON.removeMixInAnnotations(BaseClass.class);

        JSON.addMixInAnnotations(BaseClass.class, MixIn2.class);
        BaseClass base3 = JSON.parseObject("{\"ariston\":5,\"brilliant\":6}", BaseClass.class);
        Assert.assertEquals(5, base3.a);
        Assert.assertEquals(6, base3.b);
        JSON.removeMixInAnnotations(BaseClass.class);
    }
}
