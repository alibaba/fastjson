package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class MixinAPITest extends TestCase  {

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


    public void test_mixIn_get_methods() throws Exception {
        BaseClass base = new BaseClass(1, 2);

        JSON.addMixInAnnotations(BaseClass.class, MixIn1.class);
        Assert.assertEquals("{\"apple\":1,\"banana\":2}", JSON.toJSONString(base));
        Assert.assertTrue(MixIn1.class == JSON.getMixInAnnotations(BaseClass.class));

        JSON.clearMixInAnnotations();
        Assert.assertTrue(null == JSON.getMixInAnnotations(BaseClass.class));

       JSON.removeMixInAnnotations(BaseClass.class);
    }
}

