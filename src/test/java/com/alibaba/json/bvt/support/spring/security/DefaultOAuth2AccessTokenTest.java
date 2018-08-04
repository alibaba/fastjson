package com.alibaba.json.bvt.support.spring.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.util.Date;

public class DefaultOAuth2AccessTokenTest extends TestCase {
    public void test_0() throws Exception {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken("123");
        token.setExpiration(new Date());
        String json = JSON.toJSONString(token, SerializerFeature.WriteClassName);
        DefaultOAuth2AccessToken token2 = (DefaultOAuth2AccessToken) JSON.parse(json);
        assertEquals(token.getValue(), token2.getValue());
        assertEquals(token.getExpiration(), token2.getExpiration());
    }
}
