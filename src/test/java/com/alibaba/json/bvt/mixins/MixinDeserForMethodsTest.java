package com.alibaba.json.bvt.mixins;

import java.util.HashMap;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class MixinDeserForMethodsTest extends TestCase {

    static class BaseClass {
        protected HashMap<String, Object> values = new HashMap<String, Object>();

        @JSONCreator
        public BaseClass( @JSONField( name = "name" ) String name,@JSONField( name = "age" ) String age,
                @JSONField( name = "student" ) Object student ) {
            values.put( "name", name );
            values.put( "age", age );
            values.put( "student", student );
        }
    }

    static class BaseClass2 {
        protected HashMap<String, Object> values = new HashMap<String, Object>();

        public BaseClass2( String name,String age,Object student ) {
            values.put( "name", name );
            values.put( "age", age );
            values.put( "student", student );
        }
    }

    class MixIn {
        @JSONCreator
        MixIn( @JSONField( name = "name" ) String name,@JSONField( name = "age" ) String age,
                @JSONField( name = "student" ) Object student ) {
        };
    }

    public void test_0() throws Exception {
        BaseClass result = JSON.parseObject( "{ \"name\" : \"David\", \"age\" : 13, \"student\" : true }",
                BaseClass.class );
        Assert.assertNotNull( result );
        Assert.assertEquals( 3, result.values.size() );
        Assert.assertEquals( "David", result.values.get( "name" ) );
        Assert.assertEquals( "13", result.values.get( "age" ) );
        Assert.assertEquals( Boolean.TRUE, result.values.get( "student" ) );
    }

    public void test_1() throws Exception {
        JSON.addMixInAnnotations(BaseClass2.class, MixIn.class);
        BaseClass2 result = JSON.parseObject( "{ \"name\" : \"David\", \"age\" : 13, \"student\" : true }",
                BaseClass2.class );
        Assert.assertNotNull( result );
        Assert.assertEquals( 3, result.values.size() );
        Assert.assertEquals( "David", result.values.get( "name" ) );
        Assert.assertEquals( "13", result.values.get( "age" ) );
        Assert.assertEquals( Boolean.TRUE, result.values.get( "student" ) );
        JSON.removeMixInAnnotations(BaseClass2.class);
    }
}
