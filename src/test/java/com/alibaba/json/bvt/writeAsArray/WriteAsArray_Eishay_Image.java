package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import data.media.Image;
import data.media.Image.Size;

public class WriteAsArray_Eishay_Image extends TestCase {
    public void test_0 () throws Exception {
        Image image = new Image();
        image.setHeight(123);
        image.setSize(Size.LARGE);
        image.setTitle("xx");
        
        String text = JSON.toJSONString(image, SerializerFeature.BeanToArray);
        System.out.println(text);
        
        Image image2 = JSON.parseObject(text, Image.class, Feature.SupportArrayToBean);
        Assert.assertEquals(image.getHeight(), image2.getHeight());
        Assert.assertEquals(image.getWidth(), image2.getWidth());
        Assert.assertEquals(image.getSize(), image2.getSize());
        Assert.assertEquals(image.getTitle(), image2.getTitle());
        Assert.assertEquals(image.getUri(), image2.getUri());
    }
    
}
