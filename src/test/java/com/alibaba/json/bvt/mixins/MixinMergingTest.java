package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class MixinMergingTest extends TestCase
{
    public interface Contact {
        String getCity();
    }

    static class ContactImpl implements Contact {
        @Override
        public String getCity() { return "Seattle"; }
    }

    static class ContactMixin implements Contact {
        @Override
        @JSONField
        public String getCity() { return null; }
    }

    public interface Person extends Contact {}

    static class PersonImpl extends ContactImpl implements Person {}

    static class PersonMixin extends ContactMixin implements Person {}

    public void test() throws Exception {
        JSON.addMixInAnnotations(Person.class, PersonMixin.class);
        assertEquals("{\"city\":\"Seattle\"}", JSON.toJSONString(new PersonImpl()));
        JSON.removeMixInAnnotations(Person.class);
    }
}
