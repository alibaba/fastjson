package com.alibaba.json.bvt.serializer;

import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.ReferenceCodec;

public class ReferenceDeserializerTest extends TestCase {

    public void test_0() throws Exception {
        ParserConfig config = new ParserConfig();
        config.putDeserializer(MyRef.class, ReferenceCodec.instance);
        Exception error = null;
        try {
            JSON.parseObject("{\"ref\":{}}", VO.class, config, 0);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private MyRef<Object> ref;

        public MyRef<Object> getRef() {
            return ref;
        }

        public void setRef(MyRef<Object> ref) {
            this.ref = ref;
        }

    }

    public static class MyRef<T> extends WeakReference<T> {

        MyRef(T referent){
            super(referent);
        }

    }
}
