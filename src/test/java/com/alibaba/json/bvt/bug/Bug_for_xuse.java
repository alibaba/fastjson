package com.alibaba.json.bvt.bug;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AutowiredObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_xuse {
	static class Foo{
		private int id;
		private String name;
		private float price;
		private int count;
		
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		static class Ser implements AutowiredObjectSerializer{
			public void write(JSONSerializer serializer, Object object,
					Object fieldName, Type fieldType) throws IOException {
				SerializeWriter out=serializer.getWriter();
				if (object == null) {
					out.writeNull();
					return;
				}
				if (serializer.containsReference(object)) {
					serializer.writeReference(object);
					return;
				}
				
				Foo foo=(Foo)object;
		        SerialContext parent = serializer.getContext();
		        serializer.setContext(parent, object, fieldName);
		        out.write('{');
		        out.writeFieldValue((char)-1, "id", foo.id);
		        out.writeFieldValue(',', "name", foo.name);
		        out.writeFieldValue(',', "price", foo.price);
		        out.writeFieldValue(',', "count", foo.count);
		        out.writeFieldValue(',', "totalPrice", foo.price * foo.count);
		        if( out.isEnabled(SerializerFeature.PrettyFormat)){
	                serializer.println();	
		        }
                out.write('}');
			}

			public Set<Type> getAutowiredFor() {
				return Collections.<Type>singleton(Foo.class);
			}
			
		}
	}
	
	/**
	 * Use ServiceLoader to load a customized  ObjectSerializer.
	 * and the ObjectSerializer class is not a public class.
	 */
	@Test
	public void testCustomSerializer(){
		Foo foo=new Foo();
		foo.setId(1);
		foo.setName("item1");
		foo.setPrice(13.5f);
		foo.setCount(100);
		String s=JSON.toJSONString(foo);
		System.out.println(s);	
		org.junit.Assert.assertTrue(s.indexOf("\"totalPrice\":")>1);
		
		
	}

}
