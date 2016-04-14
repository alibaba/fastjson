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

public class Eishay2Encode extends BenchmarkCase {

    public final static MediaContent mediaContent = new MediaContent();

    static {
        Media media = new Media();
        media.uri = "http://javaone.com/keynote.ogg";
        media.title = null;
        media.width = 641;
        media.height = 481;
        media.format = "video/theora";
        media.duration = 18000001;
        media.size = 58982401;
        media.bitrate = 0;
        media.persons = Arrays.asList("Bill Gates, Jr.", "Steven Jobs");
        media.player = Player.FLASH;
        media.copyright = "2009, Scooby Doo";

        mediaContent.media = media;

        Image[] images = new Image[3];
  
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_huge.jpg";
            image.title = "Javaone Keynote";
            image.width = 32000;
            image.height = 24000;
            image.size = Size.LARGE;
            images[0] = image;
        }
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_large.jpg";
            image.title  = null;
            image.width = 1024;
            image.height = 768;
            image.size = Size.LARGE;
            images[1] = image;
        }
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_small.jpg";
            image.title  = null;
            image.width = 320;
            image.height = 240;
            image.size = Size.SMALL;
            images[2] = image;
        }
        mediaContent.images = images;
    }

    public Eishay2Encode(){
        super("Eishay2Encode");

    }

    @Override
    public void execute(Codec codec) throws Exception {
        String text = codec.encode(mediaContent);
        if (text == null) {
            throw new Exception();
        }
    }

}
