package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.Outter.Inner;
import com.alibaba.json.test.Outter.Inner.InnerInner;
import com.google.gson.Gson;

import junit.framework.TestCase;

class Outter{
	private String name;
	private InnerInner ii;
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public InnerInner getIi() {
		return ii;
	}


	public void setIi(InnerInner ii) {
		this.ii = ii;
	}

	class Inner{
		class InnerInner{
			private String name;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}
	}
}

public class InnerInnerTest extends TestCase{//深层内部类的序列化反序列化测试

	public void testDeserialize(){//
		String json = "{\"ii\":{\"name\":\"iicls\"},\"name\":\"ocls\"}";
		Outter o = JSON.parseObject(json, Outter.class);
		assertEquals("ocls", o.getName());
		assertEquals("iicls", o.getIi().getName());
	}
	
	public void testSerialize(){
		Outter o = new Outter();
		Inner i = o.new Inner();
		InnerInner ii = i.new InnerInner();
		ii.setName("iicls");
		o.setIi(ii);
		o.setName("ocls");
		String json = JSON.toJSONString(o);
		assertEquals("{\"ii\":{\"name\":\"iicls\"},\"name\":\"ocls\"}", json);
	}
	
	public void testGson(){
		Outter o = new Outter();
		Inner i = o.new Inner();
		InnerInner ii = i.new InnerInner();
		ii.setName("iicls");
		o.setIi(ii);
		o.setName("ocls");
		Gson gson = new Gson();//default setting
		String json = gson.toJson(o);
		assertEquals("{\"name\":\"ocls\",\"ii\":{\"name\":\"iicls\"}}", json);
		Outter newO = gson.fromJson(json, Outter.class);
		assertEquals("ocls", newO.getName());
		assertEquals("iicls", newO.getIi().getName());
	}
	
}
