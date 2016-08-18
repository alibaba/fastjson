package com.alibaba.json.bvt.awt;

import java.awt.Font;

import org.junit.Assert;
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
