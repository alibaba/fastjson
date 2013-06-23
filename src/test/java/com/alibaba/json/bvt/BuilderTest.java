package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class BuilderTest extends TestCase {
	public void test_builder() throws Exception {
		RainbowStats rainbowStats = JSON.parseObject("{\"id\":33}", RainbowStats.class);
		Assert.assertEquals(33, rainbowStats.getId());
	}
	
	private static class RainbowStats {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public RainbowStats setId(int id) {
			this.id = id;
			return this;
		}

		public String getName() {
			return name;
		}

		public RainbowStats setName(String name) {
			this.name = name;
			return this;
		}

	}
}
