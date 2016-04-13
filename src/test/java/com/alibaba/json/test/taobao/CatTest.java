package com.alibaba.json.test.taobao;

import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;

import data.taobao.cart.Banner;
import data.taobao.cart.BundleV2;
import data.taobao.cart.Cart;
import data.taobao.cart.CartResponse;
import data.taobao.cart.Footer;
import data.taobao.cart.Group;
import data.taobao.cart.ItemV2;
import data.taobao.cart.OrderBy;
import data.taobao.cart.Promotion;
import data.taobao.cart.ShopV2;
import junit.framework.TestCase;

public class CatTest extends TestCase {
private String text;
    
    protected void setUp() throws Exception {
        InputStream is = Cart.class.getClassLoader().getResourceAsStream("json/taobao/cart.json");
        text = IOUtils.toString(is);
        is.close();
    }
    
    public void test_decode() throws Exception {
        FieldTypeResolver fieldTypeResolver = new FieldTypeResolver() {
            public Type resolve(Object object, String fieldName) {
                if (fieldName.startsWith("banner_")) {
                    return Banner.class;
                }
                
                if (fieldName.startsWith("shopv2_")) {
                    return ShopV2.class;
                }
                
                if (fieldName.startsWith("itemv2_")) {
                    return ItemV2.class;
                }
                
                if (fieldName.startsWith("bundlev2_")) {
                    return BundleV2.class;
                }
                
                if (fieldName.startsWith("footer_")) {
                    return Footer.class;
                }
                
                if (fieldName.startsWith("group_")) {
                    return Group.class;
                }
                
                if (fieldName.startsWith("promotion_")) {
                    return Promotion.class;
                }
                
                if (fieldName.startsWith("orderBy")) {
                    return OrderBy.class;
                }
                
                return null;
            }
        };
        CartResponse resp = JSON.parseObject(text, CartResponse.class, fieldTypeResolver);
        Cart data = resp.data;
        Assert.assertNotNull(data);
    }

    public void test_parse() throws Exception {
        JSONObject object = JSON.parseObject(text);
        System.out.println(JSON.toJSONString(object));
    }
}
