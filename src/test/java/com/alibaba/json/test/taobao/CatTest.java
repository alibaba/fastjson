package com.alibaba.json.test.taobao;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import data.taobao.cat.Cart;
import data.taobao.cat.CartResponse;
import junit.framework.TestCase;

public class CatTest extends TestCase {
private String text;
    
    protected void setUp() throws Exception {
        InputStream is = Cart.class.getClassLoader().getResourceAsStream("json/taobao/cart.json");
        text = IOUtils.toString(is);
        is.close();
    }
    
    public void test_decode() throws Exception {
        CartResponse resp = JSON.parseObject(text, CartResponse.class);
        Cart data = resp.data;
        Assert.assertNotNull(data);
    }

    public void test_parse() throws Exception {
        JSONObject object = JSON.parseObject(text);
        System.out.println(JSON.toJSONString(object));
    }
}
