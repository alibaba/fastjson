package com.alibaba.json.bvt.bug;

import java.io.InputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;

import org.junit.Assert;
import junit.framework.TestCase;

public class Bug_for_wuyexiong extends TestCase {

    public static class Track {

        private String name;
        private String color;
        private String _abstract;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getColor() {
            return color;
        }
        
        public void setColor(String color) {
            this.color = color;
        }
        
        public String getAbstract() {
            return _abstract;
        }
        
        public void setAbstract(String _abstract) {
            this._abstract = _abstract;
        }
        
        
    }

    public static class Tracks {
        private Track[] track;

        public void setTrack(Track[] track) {
            this.track = track;
        }

        public Track[] getTrack() {
            return track;
        }
    }

    public void test_for_wuyexiong() throws Exception {
        InputStream is =  Thread.currentThread().getContextClassLoader().getResourceAsStream("wuyexiong.json");
        String text = org.apache.commons.io.IOUtils.toString(is);
        org.apache.commons.io.IOUtils.closeQuietly(is);
        
        Tracks tracks = JSON.parseObject(text, Tracks.class);
        Assert.assertEquals("Learn about developing mobile handset and tablet apps for Android.", tracks.getTrack()[0].getAbstract());
    }
}
