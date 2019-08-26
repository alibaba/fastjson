package com.alibaba.json.bvt.mixins;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class MixinDeserForClassTest extends TestCase {
    static class BaseClass {
        @JSONField( deserialize = true )
        public String a;

        @JSONField( deserialize = false, name = "a" )
        public void setA( String v ) {
            a = "XXX" + v;
        }
    }

    static class BaseClass1 {
        @JSONField( deserialize = false )
        public String a;

        @JSONField( deserialize = true, name = "a" )
        public void setA( String v ) {
            a = "XXX" + v;
        }
    }

    static class Mixin {
        @JSONField( deserialize = false )
        public String a;

        @JSONField( deserialize = true, name = "a" )
        public void setA( String v ) {
        }
    }

    public void test_1() throws Exception {
        BaseClass1 base = JSON.parseObject( "{\"a\":\"132\"}", BaseClass1.class );
        Assert.assertEquals( "XXX132", base.a );
    }

    public void test_2() throws Exception {
        JSON.addMixInAnnotations(BaseClass.class, Mixin.class);
        BaseClass base = JSON.parseObject( "{\"a\":\"132\"}", BaseClass.class );
        Assert.assertEquals( "XXX132", base.a );
        JSON.removeMixInAnnotations(BaseClass.class);
    }

}
