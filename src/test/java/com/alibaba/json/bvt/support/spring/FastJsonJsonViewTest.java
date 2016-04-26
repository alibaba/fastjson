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
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;

public class FastJsonJsonViewTest extends TestCase {

    public void test_0() throws Exception {
        FastJsonJsonView view = new FastJsonJsonView();
        
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        
        view.setFastJsonConfig(fastJsonConfig);

        Assert.assertEquals(Charset.forName("UTF-8"), fastJsonConfig.getCharset());
        fastJsonConfig.setCharset(Charset.forName("GBK"));
        Assert.assertEquals(Charset.forName("GBK"), fastJsonConfig.getCharset());

        Assert.assertNotNull(fastJsonConfig.getSerializerFeatures());
        Assert.assertEquals(0, fastJsonConfig.getSerializerFeatures().length);

        fastJsonConfig.setSerializerFeatures(SerializerFeature.BrowserCompatible);
        Assert.assertEquals(1, fastJsonConfig.getSerializerFeatures().length);
        Assert.assertEquals(SerializerFeature.BrowserCompatible, fastJsonConfig.getSerializerFeatures()[0]);

        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCheckSpecialChar, SerializerFeature.SortField);
        Assert.assertEquals(2, fastJsonConfig.getSerializerFeatures().length);
        Assert.assertEquals(SerializerFeature.DisableCheckSpecialChar, fastJsonConfig.getSerializerFeatures()[0]);
        Assert.assertEquals(SerializerFeature.SortField, fastJsonConfig.getSerializerFeatures()[1]);
        
        Map<String, Object> model = new HashMap<String, Object>();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        view.render(model, request, response);
        
        view.setRenderedAttributes(null);
        
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        view.render(model, request, response);
        
        view.setUpdateContentLength(true);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.BrowserCompatible);
        view.render(model, request, response);
        
        fastJsonConfig.setCharset(Charset.forName("GBK"));
        view.render(Collections.singletonMap("abc", "cde"), request, response);
        
        view.setDisableCaching(true);
    }
}
