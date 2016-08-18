package com.alibaba.json.bvt.parser.deser.asm;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.benchmark.encode.EishayEncode;

import data.media.MediaContent;

public class TestASMEishay extends TestCase {
    public void test_asm() throws Exception {
        String text = JSON.toJSONString(EishayEncode.mediaContent, SerializerFeature.WriteEnumUsingToString);
        System.out.println(text);
        System.out.println(text.getBytes().length);
        MediaContent object = JSON.parseObject(text, MediaContent.class);
        String text2 = JSON.toJSONString(object, SerializerFeature.WriteEnumUsingToString);
        System.out.println(text2);
    }
}
