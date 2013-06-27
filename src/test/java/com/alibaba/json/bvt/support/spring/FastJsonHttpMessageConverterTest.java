package com.alibaba.json.bvt.support.spring;

import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonHttpMessageConverterTest extends TestCase {
	public void test_() throws Exception {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		converter.setCharset(Charset.forName("UTF-8"));
		Assert.assertEquals(Charset.forName("UTF-8"), converter.getCharset());
		
		Assert.assertEquals(0, converter.getFeatures().length);
	}
}
