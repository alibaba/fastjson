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
        media.setUri("http://javaone.com/keynote.mpg");
        media.setTitle("Javaone Keynote");
        media.setWidth(640);
        media.setHeight(480);
        media.setFormat("video/mpg4");
        media.setDuration(18000000);
        media.setSize(58982400);
        media.setBitrate(262144);
        media.setPersons(Arrays.asList("Bill Gates", "Steve Jobs"));
        media.setPlayer(Player.JAVA);
        media.setCopyright(null);

        content.setMedia(media);

        List<Image> images = new ArrayList<Image>();
        {
            Image image = new Image();
            image.setUri("http://javaone.com/keynote_large.jpg");
            image.setTitle("Javaone Keynote");
            image.setWidth(1024);
            image.setHeight(768);
            image.setSize(Size.LARGE);
            images.add(image);
        }
        {
            Image image = new Image();
            image.setUri("http://javaone.com/keynote_small.jpg");
            image.setTitle("Javaone Keynote");
            image.setWidth(320);
            image.setHeight(240);
            image.setSize(Size.SMALL);
            images.add(image);
        }
        content.setImages(images);

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
