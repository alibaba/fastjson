package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.Assert;
import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class Bug_127_for_qiuyan81 extends TestCase {
	
	public void test_parserError() {
		String jsonString = "{PayStatus:0,RunEmpId:undefined}";
		Object json = JSON.parse(jsonString);
		Assert.assertEquals("{\"PayStatus\":0}",json.toString());
	}

}
