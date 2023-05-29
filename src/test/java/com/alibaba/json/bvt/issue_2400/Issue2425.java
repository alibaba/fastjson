package com.alibaba.json.bvt.issue_2400;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class Issue2425 extends TestCase {
	public void testForIssue() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> tmap = new HashMap<String, String>();
		tmap.put("k11", "v11");
		tmap.put("k12", "v12");
		map.put("k1", tmap);
		map.put("k2", "v2");

		assertEquals("[v2, v12]", JSONPath.eval(map, "$['k2','k1.k12']").toString());
		assertEquals("[v11, v2]", JSONPath.eval(map, "$['k1.k11','k2']").toString());
		assertEquals("v11", JSONPath.eval(map, "k1.k11").toString());
	}
}
