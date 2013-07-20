package com.alibaba.json.bvt.writeAsArray;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import data.media.Media;
import data.media.Media.Player;

public class WriteAsArray_Eishay_Media extends TestCase {
    public void test_0 () throws Exception {
        Media media = new Media();
        media.setHeight(123);
        media.setPlayer(Player.FLASH);
        media.setTitle("xx");
        media.setPersons(Arrays.<String>asList("a",null));
        
        String text = JSON.toJSONString(media, SerializerFeature.BeanToArray);
        System.out.println(text);
        
        Media media2 = JSON.parseObject(text, Media.class, Feature.SupportArrayToBean);
        Assert.assertEquals(media.getHeight(), media2.getHeight());
        Assert.assertEquals(media.getWidth(), media2.getWidth());
        Assert.assertEquals(media.getSize(), media2.getSize());
        Assert.assertEquals(media.getTitle(), media2.getTitle());
        Assert.assertEquals(media.getUri(), media2.getUri());
    }
    
}
