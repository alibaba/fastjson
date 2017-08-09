package com.alibaba.json.bvt.support.spring;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FastJsonJsonViewTest extends TestCase {

    @SuppressWarnings("deprecation")
    public void test_0() throws Exception {
        FastJsonJsonView view = new FastJsonJsonView();

        Assert.assertEquals(Charset.forName("UTF-8"), view.getCharset());
        view.setCharset(Charset.forName("GBK"));
        Assert.assertEquals(Charset.forName("GBK"), view.getCharset());

        Assert.assertNull(view.getDateFormat());
        view.setDateFormat("yyyyMMdd");
		
        Assert.assertNotNull(view.getFeatures());
        Assert.assertEquals(1, view.getFeatures().length);

        view.setSerializerFeature(SerializerFeature.BrowserCompatible);
        Assert.assertEquals(1, view.getFeatures().length);
        Assert.assertEquals(SerializerFeature.BrowserCompatible, view.getFeatures()[0]);

        view.setFeatures(SerializerFeature.DisableCheckSpecialChar, SerializerFeature.SortField);
        Assert.assertEquals(2, view.getFeatures().length);
        Assert.assertEquals(SerializerFeature.DisableCheckSpecialChar, view.getFeatures()[0]);
        Assert.assertEquals(SerializerFeature.SortField, view.getFeatures()[1]);
        
        view.setFilters(serializeFilter);
		Assert.assertEquals(1, view.getFilters().length);
		Assert.assertEquals(serializeFilter, view.getFilters()[0]);
		
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
        
        view.setDisableCaching(false);
        view.setUpdateContentLength(false);
        view.render(model, request, response);
        
        view.setRenderedAttributes(new HashSet<String>(Collections.singletonList("abc")));
        view.render(Collections.singletonMap("abc", "cde"), request, response);

    }
 
    public void test_1() throws Exception {
    	
        FastJsonJsonView view = new FastJsonJsonView();
        
        Assert.assertNotNull(view.getFastJsonConfig());
        view.setFastJsonConfig(new FastJsonConfig());
        
        Map<String, Object> model = new HashMap<String, Object>();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        view.render(model, request, response);
        
        view.setRenderedAttributes(null);
        view.render(model, request, response);
        
        view.setUpdateContentLength(true);
        view.render(model, request, response);
        
        view.setExtractValueFromSingleKeyModel(true);
        Assert.assertEquals(true, view.isExtractValueFromSingleKeyModel());
        
        view.setDisableCaching(true);
        view.render(Collections.singletonMap("abc", "cde"), request, response);

    }

    @Test
    public  void test_jsonp() throws Exception {
        FastJsonJsonView view = new FastJsonJsonView();

        Assert.assertNotNull(view.getFastJsonConfig());
        view.setFastJsonConfig(new FastJsonConfig());
        view.setExtractValueFromSingleKeyModel(true);
        view.setDisableCaching(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("callback", "queryName");
        MockHttpServletResponse response = new MockHttpServletResponse();


        Assert.assertEquals(true, view.isExtractValueFromSingleKeyModel());


        view.render(Collections.singletonMap("abc", "cde中文"), request, response);
        String contentAsString = response.getContentAsString();
        int contentLength = response.getContentLength();

        Assert.assertEquals(contentLength, contentAsString.getBytes(view.getFastJsonConfig().getCharset().name()).length);
    }

    @Test
    public  void test_jsonp_invalidParam() throws Exception {
        FastJsonJsonView view = new FastJsonJsonView();

        Assert.assertNotNull(view.getFastJsonConfig());
        view.setFastJsonConfig(new FastJsonConfig());
        view.setExtractValueFromSingleKeyModel(true);
        view.setDisableCaching(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("callback", "-methodName");
        MockHttpServletResponse response = new MockHttpServletResponse();


        Assert.assertEquals(true, view.isExtractValueFromSingleKeyModel());


        view.render(Collections.singletonMap("doesn't matter", Collections.singletonMap("abc", "cde中文")), request, response);
        String contentAsString = response.getContentAsString();
        Assert.assertTrue(contentAsString.startsWith("{\"abc\":\"cde中文\"}"));

    }
    
    private SerializeFilter serializeFilter = new ValueFilter() {

		public Object process(Object object, String name, Object value) {
			if (value == null) {
				return "";
			}
			if (value instanceof Number) {
				return String.valueOf(value);
			}
			return value;
		}
	};
}