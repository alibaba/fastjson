package com.alibaba.json.bvt;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;
import junit.framework.TestCase;

public class ModuleTest extends TestCase {
    public void test_for_module() throws Exception {
        ParserConfig config = new ParserConfig();
        config.register(new MyModuel2());
        config.register(new MyModuel());

        assertSame(MiscCodec.instance, config.getDeserializer(A.class));
    }

    public void test_for_module_1() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.register(new MyModuel2());
        config.register(new MyModuel());

        assertSame(MiscCodec.instance, config.getObjectWriter(A.class));
    }

    public static class A {

    }

    public static class MyModuel implements Module {

        @Override
        public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
            return MiscCodec.instance;
        }

        @Override
        public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
            return MiscCodec.instance;
        }
    }

    public static class MyModuel2 implements Module {

        @Override
        public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
            return null;
        }

        @Override
        public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
            return null;
        }
    }
}
