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

public class Eishay3Encode extends BenchmarkCase {

    public final static MediaContent mediaContent = new MediaContent();

    static {
        Media media = new Media();
        media.uri = "http://javaone.com/keynote.mpglkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
        media.title = "Javaone Keynotelkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
        media.width = 640;
        media.height = 480;
        media.format = "video/mpg4lkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
        media.duration = 18000000;
        media.size = 58982400;
        media.bitrate = 262144;
        media.persons = Arrays.asList("Bill Gateslkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj", "Steve Jobslkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj");
        media.player = Player.JAVA;
        media.copyright = null;

        mediaContent.media = media;

        Image[] images = new Image[2];
  
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_large.jpglkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
            image.title  = "Javaone Keynotelkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
            image.width = 1024;
            image.height = 768;
            image.size = Size.LARGE;
            images[0] = image;
        }
        {
            Image image = new Image();
            image.uri = "http://javaone.com/keynote_small.jpglkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
            image.title  = "Javaone Keynotelkajldfjlskajdflkjslfjdslfjldjfljsdfljsdlfjsljfldjfldjals;djfasldjf;alskdjf;aslkdjf;asdjf;laskdjflsjdalfjd;alksjdfl;jsa;lfdja;slkdjf;alsjfd;lajsfl;dj";
            image.width = 320;
            image.height = 240;
            image.size = Size.SMALL;
            images[1] = image;
        }
        mediaContent.images = images;
    }

    public Eishay3Encode(){
        super("Eishay3Encode");

    }

    @Override
    public void execute(Codec codec) throws Exception {
        String text = codec.encode(mediaContent);
        if (text == null) {
            throw new Exception();
        }
    }

}
