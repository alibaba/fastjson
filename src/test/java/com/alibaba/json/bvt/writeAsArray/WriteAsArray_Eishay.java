package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.benchmark.decode.EishayDecodeBytes;

import data.media.MediaContent;

public class WriteAsArray_Eishay extends TestCase {
    public void test_0 () throws Exception {
        MediaContent content = EishayDecodeBytes.instance.getContent();
        
        String text = JSON.toJSONString(content, SerializerFeature.BeanToArray);
        System.out.println(text.getBytes().length);
        JSON.parseObject(text, MediaContent.class, Feature.SupportArrayToBean);
    }
    
    public static class VO {
        private short id;
        private String name;

        public short getId() {
            return id;
        }

        public void setId(short id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
