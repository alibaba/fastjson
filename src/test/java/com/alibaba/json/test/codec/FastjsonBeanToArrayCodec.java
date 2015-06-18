package com.alibaba.json.test.codec;

import java.util.Collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastjsonBeanToArrayCodec implements Codec {

    private ParserConfig    config = ParserConfig.getGlobalInstance();

    public String getName() {
        return "fastjson-bean-to-array";
    }

    public <T> T decodeObject(String text, Class<T> clazz) {
        DefaultExtJSONParser parser = new DefaultExtJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        parser.config(Feature.SupportArrayToBean, true);
        return parser.parseObject(clazz);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        parser.config(Feature.SupportArrayToBean, true);
        return parser.parseArray(clazz);
    }

    public final Object decodeObject(String text) {
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        parser.config(Feature.SupportArrayToBean, true);
        return parser.parse();
    }

    public final Object decode(String text) {
        DefaultJSONParser parser = new DefaultJSONParser(text, config);
        parser.config(Feature.DisableCircularReferenceDetect, true);
        parser.config(Feature.SupportArrayToBean, true);
        return parser.parse();
    }

    // private JavaBeanSerializer serializer = new JavaBeanSerializer(Long_100_Entity.class);

    public String encode(Object object) throws Exception {
        SerializeWriter out = new SerializeWriter();
        out.config(SerializerFeature.DisableCircularReferenceDetect, true);
        out.config(SerializerFeature.BeanToArray, true);
//        out.config(SerializerFeature.DisableCheckSpecialChar, true);

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(object);

        String text = out.toString();

        out.close();

        return text;
    }

    @SuppressWarnings("unchecked")
    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        return (T) JSON.parseObject(input, clazz, Feature.SupportArrayToBean, Feature.DisableCircularReferenceDetect);
    }

}
