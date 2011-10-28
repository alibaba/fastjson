package com.alibaba.json.bvt;

import java.awt.Font;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class FontTest extends TestCase {

    public void test_color() throws Exception {
        Font[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font font : fonts) {
            String text = JSON.toJSONString(font);

            Font font2 = JSON.parseObject(text, Font.class);

            Assert.assertEquals(font, font2);
        }
    }
}
