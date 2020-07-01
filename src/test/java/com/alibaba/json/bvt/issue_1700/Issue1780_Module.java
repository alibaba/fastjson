package com.alibaba.json.bvt.issue_1700;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;

import junit.framework.TestCase;

public class Issue1780_Module extends TestCase {

	public void test_for_issue() {
		org.json.JSONObject req = new org.json.JSONObject();

		SerializeConfig config = new SerializeConfig();
		config.register(new myModule());
		req.put("id", 1111);
		req.put("name", "name11");
		Assert.assertEquals("{\"name\":\"name11\",\"id\":1111}", JSON.toJSONString(req, config));
	}

	public class myModule implements Module {

		@SuppressWarnings("rawtypes")
		@Override
		public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
			return null;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
			return new ObjectSerializer() {

				@Override
				public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
						int features) throws IOException {
					System.out.println("-------------myModule.createSerializer-------------------");
					org.json.JSONObject req = (org.json.JSONObject) object;
					serializer.out.write(req.toString());
				}
			};
		}

	}
}
