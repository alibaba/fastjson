package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class MixinInheritanceTest extends TestCase {
    static class Beano {
        public int ido = 42;
        public String nameo = "Bob";
    }

    static class BeanoMixinSuper {
        @JSONField(name = "name")
        public String nameo;
    }

    static class BeanoMixinSub extends BeanoMixinSuper {
        @JSONField(name = "id")
        public int ido;
    }

    static class Beano2 {
        public int getIdo() { return 13; }
        public String getNameo() { return "Bill"; }
    }

    static abstract class BeanoMixinSuper2 extends Beano2 {
        @Override
        @JSONField(name = "name")
        public abstract String getNameo();
    }

    static abstract class BeanoMixinSub2 extends BeanoMixinSuper2 {
        @Override
        @JSONField(name = "id")
        public abstract int getIdo();
    }

    public void test_field() throws Exception {
        JSON.addMixInAnnotations(Beano.class, BeanoMixinSub.class);
        String str = JSON.toJSONString(new Beano());
        JSONObject result = JSONObject.parseObject(str);
        assertEquals(2, result.size());
        if (!result.containsKey("id")
                || !result.containsKey("name")) {
            fail("Should have both 'id' and 'name', but content = " + result);
        }
        JSON.removeMixInAnnotations(Beano.class);
    }

    public void test_method() throws Exception {
        JSON.addMixInAnnotations(Beano2.class, BeanoMixinSub2.class);
        String str = JSON.toJSONString(new Beano2());
        JSONObject result = JSONObject.parseObject(str);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("id"));
        assertTrue(result.containsKey("name"));
        JSON.removeMixInAnnotations(Beano2.class);
    }

    static class BaseClass {
        public int a;
        public int b;
        public int c;

        public  BaseClass() {

        }
        public BaseClass(int a, int b,int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    class BaseMixIn {
        @JSONField(name = "apple")
        public int a;
        @JSONField(name = "banana")
        public int b;
    }

    class SubMixIn extends BaseMixIn {
        @JSONField(name = "pear")
        public int c;
    }

    class SubMixIn1 extends SubMixIn {
        @JSONField(name = "watermelon")
        public int b;
    }

    public void test_mixIn_extend() throws Exception {
        BaseClass base = new BaseClass(1, 2,3);
        Assert.assertEquals("{\"a\":1,\"b\":2,\"c\":3}", JSON.toJSONString(base));

        JSON.addMixInAnnotations(BaseClass.class, SubMixIn.class);
        Assert.assertEquals("{\"apple\":1,\"banana\":2,\"pear\":3}", JSON.toJSONString(base));
        JSON.removeMixInAnnotations(BaseClass.class);
    }

    public void test_mixIn_extend1() throws Exception {
        BaseClass base = new BaseClass(1, 2,3);
        Assert.assertEquals("{\"a\":1,\"b\":2,\"c\":3}", JSON.toJSONString(base));

        JSON.addMixInAnnotations(BaseClass.class, SubMixIn1.class);
        Assert.assertEquals("{\"apple\":1,\"pear\":3,\"watermelon\":2}", JSON.toJSONString(base));
        JSON.removeMixInAnnotations(BaseClass.class);
    }
}
