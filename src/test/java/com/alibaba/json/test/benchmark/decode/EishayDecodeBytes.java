package com.alibaba.json.test.benchmark.decode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

import data.media.Image;
import data.media.Image.Size;
import data.media.Media;
import data.media.Media.Player;
import data.media.MediaContent;

public class EishayDecodeBytes extends BenchmarkCase {

    public final static EishayDecodeBytes instance = new EishayDecodeBytes();

    private final byte[]                  bytes;
    private final char[]                  chars;
    private final String                  text;

    private final MediaContent                  content;

    public byte[] getBytes() {
        return bytes;
    }

    public char[] getChars() {
        return chars;
    }

    public String getText() {
        return text;
    }

    public MediaContent getContent() {
        return content;
    }

    public EishayDecodeBytes(){
        super("EishayDecode-Byte[]");

        content = new MediaContent();

        Media media = new Media();
        media.uri = "http://javaone.com/keynote.mpg";
        media.title  = "Javaone Keynote";
        media.width = 640;
        media.height = 480;
        media.format = "video/mpg4";
        media.duration = 18000000;
        media.size = 58982400;
        media.bitrate = 262144;
        media.persons = Arrays.asList("Bill Gates", "Steve Jobs");
        media.player = Player.JAVA;
        media.copyright = null;

        content.media = media;

        Image[] images = new Image[2];
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_large.jpg";
            image.title = "Javaone Keynote";
            image.width = 1024;
            image.height = 768;
            image.size = Size.LARGE;
            images[0] = image;
        }
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_small.jpg";
            image.title = "Javaone Keynote";
            image.width = 320;
            image.height = 240;
            image.size = Size.SMALL;
            images[1] = image;
        }
        content.images  = images;

        try {
            text = JSON.toJSONString(content, SerializerFeature.WriteEnumUsingToString, SerializerFeature.SortField);
            chars = (text + " ").toCharArray();
            bytes = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decodeObject(bytes, MediaContent.class);
    }

}
