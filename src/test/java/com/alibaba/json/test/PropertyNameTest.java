package com.alibaba.json.test;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class PropertyNameTest  extends TestCase {

	  public void testProperty() throws Exception {
		  Entity entity = new Entity();
		  entity.setName("fastjson");
		  entity.setValue(1);
		  String text =  JSON.toJSON(entity).toString();
		  Assert.assertEquals("{\"value\":1,\"name\":\"fastjson\"}", text);
	  }
	  
	 public static class Entity {
	        private int value;
	        private String name;
	        
	        
	        public int getValue() {
	            return value;
	        }

	        public void setValue(int value) {
	            this.value = value;
	        }

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
	        
			public String getOther() {
				return "Other";
			}

	    }
}
