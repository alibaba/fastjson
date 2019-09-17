package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

public class JSONFieldTest6 extends TestCase {

    public void test_for_issue1()
    {
        NonStringMap nonStringMap = new NonStringMap();
        Map<Integer, Integer> map1 = new HashMap();
        map1.put( 111,666 );
        nonStringMap.setMap1( map1 );
        String json = JSON.toJSONString( nonStringMap );
        assertEquals( "{\"map1\":{\"111\":666}}", json );
    }

    public void test_for_issue2()
    {
        NonStringMap nonStringMap = new NonStringMap();
        Map<Integer, Integer> map2 = new HashMap();
        map2.put( 222,888 );
        nonStringMap.setMap2( map2 );
        String json = JSON.toJSONString( nonStringMap );
        assertEquals( "{\"map2\":{222:\"888\"}}", json );
    }

    public void test_for_issue3()
    {
        NonStringMap nonStringMap = new NonStringMap();
        Map<Integer, Integer> map3 = new HashMap();
        map3.put( 333,999 );
        nonStringMap.setMap3( map3 );
        String json = JSON.toJSONString( nonStringMap );
        assertEquals( "{\"map3\":{\"333\":\"999\"}}", json );
    }

    public void test_for_issue4()
    {
        NonStringMap nonStringMap = new NonStringMap();
        Bean person = new Bean();
        person.setAge( 23 );
        nonStringMap.setPerson( person );
        String json = JSON.toJSONString( nonStringMap );
        assertEquals( "{\"person\":{\"age\":\"23\"}}", json );
    }

    class NonStringMap
    {
        @JSONField( serialzeFeatures = {SerializerFeature.WriteNonStringKeyAsString} )
        private Map map1;

        public Map getMap1()
        {
            return map1;
        }

        public void setMap1( Map map1 )
        {
            this.map1 = map1;
        }

        @JSONField( serialzeFeatures = {SerializerFeature.WriteNonStringValueAsString} )
        private Map map2;

        public Map getMap2()
        {
            return map2;
        }

        public void setMap2( Map map2 )
        {
            this.map2 = map2;
        }

        @JSONField( serialzeFeatures = {SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.WriteNonStringValueAsString} )
        private Map map3;

        public Map getMap3()
        {
        return map3;
        }

        public void setMap3( Map map3 )
        {
            this.map3 = map3;
        }

        @JSONField( serialzeFeatures = {SerializerFeature.WriteNonStringValueAsString} )
        private Bean person;

        public Bean getPerson()
        {
            return person;
        }

        public void setPerson( Bean person )
        {
            this.person = person;
        }
    }

    class Bean {
        private int age;

        public int getAge()
        {
            return age;
        }

        public void setAge( int age )
        {
            this.age = age;
        }
    }
}
