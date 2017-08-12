package com.alibaba.json.bvt.support.spring.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedCookie;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import java.lang.reflect.Field;

/**
 * Created by wenshao on 11/08/2017.
 */
public class DefaultSavedRequestTest extends TestCase {
    public void test_for_issue() throws Exception {
        MockHttpServletRequest mockReq = new MockHttpServletRequest();
        DefaultSavedRequest request = new DefaultSavedRequest(mockReq, new PortResolver() {

            public int getServerPort(ServletRequest servletRequest) {
                return 0;
            }
        });

        String str = JSON.toJSONString(request, SerializerFeature.WriteClassName);
//        System.out.println(str);

        Field field = GenericFastJsonRedisSerializer.class.getDeclaredField("defaultRedisConfig");
        field.setAccessible(true);
        ParserConfig config = (ParserConfig) field.get(null);
        JSON.parseObject(str, Object.class, config);

        JSON.parseObject(str);
    }


    public void test_cookie() throws Exception {
        String json = "{\"name\":\"xx\",\"value\":\"xx\",\"comment\":\"xx\",\"domain\":\"xx\"}";
        SavedCookie cookie = JSON.parseObject(json, SavedCookie.class);
        assertEquals("xx", cookie.getName());
        assertEquals("{\"comment\":\"xx\",\"cookie\":{\"comment\":\"xx\",\"domain\":\"xx\",\"httpOnly\":false,\"maxAge\":0,\"name\":\"xx\",\"secure\":false,\"value\":\"xx\",\"version\":0},\"domain\":\"xx\",\"maxAge\":0,\"name\":\"xx\",\"secure\":false,\"value\":\"xx\",\"version\":0}", JSON.toJSONString(cookie));
    }

    public void test_0() throws Exception {
        DefaultCsrfToken token = JSON.parseObject("{\"token\":\"xxx\",\"parameterName\":\"222\",\"headerName\":\"hhh\"}", DefaultCsrfToken.class);
        assertEquals("hhh", token.getHeaderName());
        assertEquals("222", token.getParameterName());
        assertEquals("xxx", token.getToken());
        assertEquals("{\"headerName\":\"hhh\",\"parameterName\":\"222\",\"token\":\"xxx\"}", JSON.toJSONString(token));
    }

    public void test_http_cookie() throws Exception {
        Cookie cookie = new Cookie("cna", "h8a2EO57uEgCAXyg1TgBBFK");
        cookie.setMaxAge(10);
        String json = JSON.toJSONString(cookie);
        Cookie cookie1 = JSON.parseObject(json, Cookie.class);
        assertEquals(cookie.getName(), cookie1.getName());
        assertEquals(cookie.getValue(), cookie1.getValue());
        assertEquals(cookie.getMaxAge(), cookie1.getMaxAge());
        //System.out.println(json);
    }
}
