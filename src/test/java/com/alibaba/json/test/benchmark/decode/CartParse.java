package com.alibaba.json.test.benchmark.decode;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

/**
 * Created by wenshao on 16/4/4.
 */
public class CartParse extends BenchmarkCase {

    private static String text;

    public CartParse(){
        super("CartParse");

        // JavaBeanMapping.getGlobalInstance().putDeserializer(Image.class, new ImageDeserializer());
        // JavaBeanMapping.getGlobalInstance().putDeserializer(Media.class, new MediaDeserializer());
    }

    public void init(Codec codec) throws Exception {
        if (text != null) {
            return;
        }
        
        InputStream is = CartParse.class.getClassLoader().getResourceAsStream("json/taobao/cart.json");
        text = IOUtils.toString(is);
        System.out.println("cartJsonObject " + text.substring(100));
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decode(text);
    }

}