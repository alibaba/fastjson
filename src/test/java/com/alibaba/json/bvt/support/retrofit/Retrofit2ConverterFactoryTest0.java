package com.alibaba.json.bvt.support.retrofit;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.retrofit.Retrofit2ConverterFactory;
import junit.framework.TestCase;

public class Retrofit2ConverterFactoryTest0 extends TestCase {
    public void test_for_coverage() throws Exception {
        Retrofit2ConverterFactory f = new Retrofit2ConverterFactory();
        f.getParserConfig();
        f.getParserFeatures();
        f.getParserFeatureValues();
        f.getSerializeConfig();
        f.getSerializerFeatures();
        f.setParserConfig(ParserConfig.getGlobalInstance());
        f.setParserFeatures(new Feature[0]);
        f.setParserFeatureValues(0);
        f.setSerializeConfig(SerializeConfig.globalInstance);
        f.setSerializerFeatures(new SerializerFeature[0]);
        f.requestBodyConverter(Model.class, null, null, null);
        f.responseBodyConverter(Model.class, null, null);
    }

    public static class Model {

    }
}
