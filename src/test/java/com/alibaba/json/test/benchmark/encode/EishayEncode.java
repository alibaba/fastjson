package com.alibaba.json.test.benchmark.encode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

import data.media.Image;
import data.media.Image.Size;
import data.media.Media;
import data.media.Media.Player;
import data.media.MediaContent;

public class EishayEncode extends BenchmarkCase {

    public final static MediaContent mediaContent = new MediaContent();

    static {
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

        mediaContent.media = media;

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
        mediaContent.images = images;
    }

    public EishayEncode(){
        super("EishayEncode");

    }

    @Override
    public void execute(Codec codec) throws Exception {
        String text = codec.encode(mediaContent);
        if (text == null) {
            throw new Exception();
        }
    }

}
