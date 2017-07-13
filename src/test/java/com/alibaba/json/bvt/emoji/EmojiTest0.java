package com.alibaba.json.bvt.emoji;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;

/**
 * Created by wenshao on 13/04/2017.
 */
public class EmojiTest0 extends TestCase {
    public void test_for_emoji() throws Exception {
        Model model = new Model();
        model.value = "An 😀awesome 😃string with a few 😉emojis!";

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JSON.writeJSONString(out, model);

        String text = new String(out.toByteArray(), "UTF-8");
        System.out.println(text);
    }

    public static class Model {
        public String value;
    }
}
