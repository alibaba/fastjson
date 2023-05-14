package com.alibaba.json.bvt.issue_2100;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.spi.Module;

import junit.framework.TestCase;

public class Issue2179 extends TestCase {

	// 场景：序列化
	public void test_for_issue() throws Exception {
		Model1 model = new Model1(ProductType1.Phone, ProductType1.Computer);
		String out = "{\"l_k_assbalv4\":{\"code\":1,\"prompt\":\"手机\"},\"type1\":{\"code\":2,\"prompt\":\"电脑\"}}";
		Assert.assertEquals(out, JSON.toJSONString(model));
	}

	// 场景：使用@JSONType的deserializer = EnumAwareSerializer1.class测试自定义反序列化器
	public void test_for_issue2() {
		String str = "{\"l_k_assbalv4\":{\"code\":1,\"prompt\":\"手机\"},\"type1\":{\"code\":2,\"prompt\":\"电脑\"}}";
		Model1 model = JSON.parseObject(str, Model1.class);
		String out = "{\"l_k_assbalv4\":{\"code\":1,\"prompt\":\"手机\"},\"type1\":{\"code\":2,\"prompt\":\"电脑\"}}";
		Assert.assertEquals(out, JSON.toJSONString(model));
	}

	// 场景：使用@JSONField的deserializeUsing = EnumAwareSerializer2.class测试自定义测试自定义反序化器
	public void test_for_issue3() {
		// l_k_assbalv4对应Model2中的Type走自定义，type1走默认枚举反序列化
		String str = "{\"l_k_assbalv4\":{\"code\":1,\"prompt\":\"手机\"},\"type1\":\"Computer\"}";
		Model2 model = JSON.parseObject(str, Model2.class);
		String out = "{\"l_k_assbalv4\":{\"code\":1,\"prompt\":\"手机\"},\"type1\":{\"code\":2,\"prompt\":\"电脑\"}}";
		Assert.assertEquals(out, JSON.toJSONString(model));
	}

	// 场景：使用Module
	public void test_for_issue4() {
		ParserConfig config = new ParserConfig();
		config.register(new MyModuel());

		String str = "{\"type\":\"Phone\",\"type1\":\"Computer\"}";
		Model3 model = JSON.parseObject(str, Model3.class, config);
		String out = "{\"type\":{\"code\":2,\"prompt\":\"电脑\"},\"type1\":{\"code\":1,\"prompt\":\"手机\"}}";
		Assert.assertEquals(out, JSON.toJSONString(model));
	}

	interface EnumAware {
		int getCode();

		String getPrompt();
	}

	@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumAwareSerializer1.class)
	public static enum ProductType1 implements EnumAware {
		Phone(1, "手机"), Computer(2, "电脑");

		public final int code;
		public final String prompt;

		ProductType1(int code, String prompt) {
			this.code = code;
			this.prompt = prompt;
		}

		@Override
		public int getCode() {
			return this.code;
		}

		@Override
		public String getPrompt() {
			return this.prompt;
		}

		public static ProductType1 get(int code) {
			switch (code) {
			case 1:
				return Phone;
			case 2:
				return Computer;
			default:
				return null;
			}
		}
	}

	public static class Model1 {
		@JSONField(name = "l_k_assbalv4")
		private ProductType1 type;
		private ProductType1 type1;

		public Model1(ProductType1 type, ProductType1 type1) {
			this.type = type;
			this.type1 = type1;
		}

		public ProductType1 getType() {
			return type;
		}

		public void setType(ProductType1 type) {
			this.type = type;
		}

		public ProductType1 getType1() {
			return type1;
		}

		public void setType1(ProductType1 type1) {
			this.type1 = type1;
		}
	}

	@JSONType(serializeEnumAsJavaBean = true)
	public static enum ProductType2 implements EnumAware {
		Phone(1, "手机"), Computer(2, "电脑");

		public final int code;
		public final String prompt;

		ProductType2(int code, String prompt) {
			this.code = code;
			this.prompt = prompt;
		}

		@Override
		public int getCode() {
			return this.code;
		}

		@Override
		public String getPrompt() {
			return this.prompt;
		}

		public static ProductType2 get(int code) {
			switch (code) {
			case 1:
				return Phone;
			case 2:
				return Computer;
			default:
				return null;
			}
		}
	}

	public static class Model2 {
		@JSONField(name = "l_k_assbalv4", deserializeUsing = EnumAwareSerializer2.class)
		private ProductType2 type;
		private ProductType2 type1;

		public Model2(ProductType2 type, ProductType2 type1) {
			this.type = type;
			this.type1 = type1;
		}

		public ProductType2 getType() {
			return type;
		}

		public void setType(ProductType2 type) {
			this.type = type;
		}

		public ProductType2 getType1() {
			return type1;
		}

		public void setType1(ProductType2 type1) {
			this.type1 = type1;
		}
	}

	@JSONType(serializeEnumAsJavaBean = true)
	public static enum ProductType3 implements EnumAware {
		Phone(1, "手机"), Computer(2, "电脑");

		public final int code;
		public final String prompt;

		@Override
		public int getCode() {
			return this.code;
		}

		ProductType3(int code, String prompt) {
			this.code = code;
			this.prompt = prompt;
		}

		@Override
		public String getPrompt() {
			return this.prompt;
		}

		public static ProductType3 get(int code) {
			switch (code) {
			case 1:
				return Phone;
			case 2:
				return Computer;
			default:
				return null;
			}
		}
	}

	public static class Model3 {
		private ProductType3 type;
		private ProductType3 type1;

		public Model3(ProductType3 type, ProductType3 type1) {
			this.type = type;
			this.type1 = type1;
		}

		public ProductType3 getType() {
			return type;
		}

		public void setType(ProductType3 type) {
			this.type = type;
		}

		public ProductType3 getType1() {
			return type1;
		}

		public void setType1(ProductType3 type1) {
			this.type1 = type1;
		}
	}

	public static class EnumAwareSerializer1 implements ObjectDeserializer {
		@SuppressWarnings("unchecked")
		public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
			String val = StringCodec.instance.deserialze(parser, type, fieldName);
			System.out.println("-----------------EnumAwareSerializer1.deserialze-----------------------------");
			System.out.println(val);
			return (T) ProductType1.get(JSON.parseObject(val).getInteger("code"));
		}

		@Override
		public int getFastMatchToken() {
			return JSONToken.LITERAL_STRING;
		}
	}

	public static class EnumAwareSerializer2 implements ObjectDeserializer {
		@SuppressWarnings("unchecked")
		public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
			String val = StringCodec.instance.deserialze(parser, type, fieldName);
			System.out.println("-----------------EnumAwareSerializer2.deserialze-----------------------------");
			System.out.println(val);
			return (T) ProductType2.get(JSON.parseObject(val).getInteger("code"));
		}

		@Override
		public int getFastMatchToken() {
			return JSONToken.LITERAL_STRING;
		}
	}

	public static class MyModuel implements Module {

		@SuppressWarnings("rawtypes")
		@Override
		public ObjectDeserializer createDeserializer(ParserConfig config, Class type) {
			return new ObjectDeserializer() {
				@SuppressWarnings("unchecked")
				@Override
				public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
					String val = StringCodec.instance.deserialze(parser, type, fieldName);
					System.out.println("-----------MyModuel.deserialze------------------------");
					System.out.println(val);
					try {
						Constructor c = Class.forName(type.getTypeName()).getDeclaredConstructor(ProductType3.class,
								ProductType3.class);
						return (T) c.newInstance(ProductType3.Computer, ProductType3.Phone);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public int getFastMatchToken() {
					return JSONToken.LITERAL_STRING;
				}
			};
		}

		@SuppressWarnings("rawtypes")
		@Override
		public ObjectSerializer createSerializer(SerializeConfig config, Class type) {
			return new ObjectSerializer() {
				@Override
				public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
						int features) throws IOException {
					SerializeWriter out = serializer.out;
					if (object == null) {
						out.writeNull();
						return;
					}
					System.err.println("--------------MyModuel.write-------------------------");

					StringCodec.instance.write(serializer, ((ProductType3) object).name(), fieldName, fieldType,
							features);
				}
			};
		}
	}

}
