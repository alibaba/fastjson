package com.alibaba.json.bvt.writeAsArray;

import java.util.Arrays;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import data.media.Media;
import data.media.Media.Player;
import junit.framework.TestCase;

public class WriteAsArray_Eishay_Media extends TestCase {
    public void test_0 () throws Exception {
        Media media = new Media();
        media.height = 123;
        media.player = Player.FLASH;
        media.title = "xx";
        media.persons = Arrays.<String>asList("a",null);
        
        String text = JSON.toJSONString(media, SerializerFeature.BeanToArray);
        System.out.println(text);
        
        Media media2 = JSON.parseObject(text, Media.class, Feature.SupportArrayToBean);
        Assert.assertEquals(media.height, media2.height);
        Assert.assertEquals(media.width, media2.width);
        Assert.assertEquals(media.size, media2.size);
        Assert.assertEquals(media.title, media2.title);
        Assert.assertEquals(media.uri, media2.uri);
    }
    
}
