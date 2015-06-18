package com.alibaba.json.bvt.bug;

import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_105_for_SpitFire extends TestCase {

    static private class Foo {

        private List<String> names;
        private List<String> codes;

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }

        public List<String> getCodes() {
            return codes;
        }

        public void setCodes(List<String> codes) {
            this.codes = codes;
        }

    }

    public void test_listErrorTest() {
        Foo foo = new Foo();
        String json = JSON.toJSONString(foo, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
        Foo f = JSON.parseObject(json, Foo.class);
        System.out.println(f);
    }

}
