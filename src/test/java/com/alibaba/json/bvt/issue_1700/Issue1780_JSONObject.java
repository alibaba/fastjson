package com.alibaba.json.bvt.issue_1700;

import org.junit.Assert;
import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1780_JSONObject extends TestCase {

	public void test_for_issue() {
		org.json.JSONObject req = new org.json.JSONObject();
		req.put("id", 1111);
		req.put("name", "name11");
		Assert.assertEquals("{\"name\":\"name11\",\"id\":1111}", JSON.toJSONString(req));
	}
}
