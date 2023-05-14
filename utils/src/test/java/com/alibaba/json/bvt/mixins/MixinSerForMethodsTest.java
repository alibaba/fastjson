package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class MixinSerForMethodsTest extends TestCase {

    @SuppressWarnings( "unused" )
    static class BaseClass {
        private String a;
        private String b;

        protected BaseClass() {
        }

        public BaseClass( String a,String b ) {
            this.a = a;
            this.b = b;
        }

        @JSONField( name = "b" )
        public String takeB() {
            return b;
        }
    }

    static class BaseClass2 {
        private String a;
        private String b;

        protected BaseClass2() {
        }

        public BaseClass2( String a,String b ) {
            this.a = a;
            this.b = b;
        }

        @JSONField( name = "b" )
        public String takeB() {
            return b;
        }

        @JSONField( name = "a" )
        public String takeA() {
            return a;
        }
    }

    abstract static class MixIn {
        String a;

        @JSONField( name = "b2" )
        public abstract String takeB();

        abstract String takeA();
    }

    public void test() throws Exception{
        BaseClass bean = new BaseClass( "a1", "b2" );

        String jsonString = JSON.toJSONString( bean );
        JSONObject result = JSON.parseObject( jsonString );
        assertEquals( 1, result.size() );
        assertEquals( "b2", result.get( "b" ) );

        BaseClass2 bean2 = new BaseClass2( "a1", "b2" );
        JSON.addMixInAnnotations( BaseClass2.class, MixIn.class );
        jsonString = JSON.toJSONString( bean2 );
        result = JSON.parseObject( jsonString );
        assertEquals( 2, result.size() );
        assertEquals( "b2", result.get( "b2" ) );
        assertEquals( "a1", result.get( "a" ) );
        JSON.removeMixInAnnotations( BaseClass.class );
    }
}