package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import data.media.Image;
import data.media.Image.Size;

public class WriteAsArray_Eishay_Image extends TestCase {
    public void test_0()  throws Exception {
        Image image = new Image();
        image.height = 123;
        image.size =Size.LARGE;
        image.title ="xx";
        
        String text = JSON.toJSONString(image, SerializerFeature.BeanToArray);
        System.out.println(text);
        
        Image image2 = JSON.parseObject(text, Image.class, Feature.SupportArrayToBean);
        Assert.assertEquals(image.height, image2.height);
        Assert.assertEquals(image.width, image2.width);
        Assert.assertEquals(image.size, image2.size);
        Assert.assertEquals(image.title, image2.title);
        Assert.assertEquals(image.uri, image2.uri);
    }
    
}
