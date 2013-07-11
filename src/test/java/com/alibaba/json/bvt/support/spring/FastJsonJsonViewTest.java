package com.alibaba.json.bvt.support.spring;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;

public class FastJsonJsonViewTest extends TestCase {

    @SuppressWarnings("deprecation")
    public void test_0() throws Exception {
        FastJsonJsonView view = new FastJsonJsonView();

        Assert.assertEquals(Charset.forName("UTF-8"), view.getCharset());
        view.setCharset(Charset.forName("GBK"));
        Assert.assertEquals(Charset.forName("GBK"), view.getCharset());

        Assert.assertNotNull(view.getFeatures());
        Assert.assertEquals(0, view.getFeatures().length);

        view.setSerializerFeature(SerializerFeature.BrowserCompatible);
        Assert.assertEquals(1, view.getFeatures().length);
        Assert.assertEquals(SerializerFeature.BrowserCompatible, view.getFeatures()[0]);

        view.setFeatures(SerializerFeature.DisableCheckSpecialChar, SerializerFeature.SortField);
        Assert.assertEquals(2, view.getFeatures().length);
        Assert.assertEquals(SerializerFeature.DisableCheckSpecialChar, view.getFeatures()[0]);
        Assert.assertEquals(SerializerFeature.SortField, view.getFeatures()[1]);
        
        Map<String, Object> model = new HashMap<String, Object>();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        view.render(model, request, response);
        
        view.setRenderedAttributes(null);
        
        view.setCharset(Charset.forName("UTF-8"));
        view.render(model, request, response);
        
        view.setUpdateContentLength(true);
        view.setFeatures(SerializerFeature.BrowserCompatible);
        view.render(model, request, response);
        
        view.setCharset(Charset.forName("GBK"));
        view.render(Collections.singletonMap("abc", "cde"), request, response);
        
        view.setDisableCaching(true);
    }
}
