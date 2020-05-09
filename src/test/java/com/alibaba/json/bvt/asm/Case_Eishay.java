package com.alibaba.json.bvt.asm;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;

import data.media.MediaContent;

public class Case_Eishay extends TestCase {

    private final String text;

    public Case_Eishay(){
        super();
        this.text = EishayDecodeBytes.instance.getText();
    }

    public void test_0() throws Exception {
        //JavaBeanMapping.getGlobalInstance().setAsmEnable(false);
        System.out.println(text);
        MediaContent object = JSON.parseObject(text, MediaContent.class);
        String text2 = JSON.toJSONString(object, SerializerFeature.WriteEnumUsingToString);
        System.out.println(text2);
        System.out.println(JSON.toJSONString(JSON.parseObject(text2), true));
    }

}
