package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class MixinJSONTypeTest extends TestCase {
	public void test_1() {
		User user1 = new User("zhangsan", "male", 19);
		Assert.assertEquals("{\"age\":19,\"sex\":\"male\",\"userName\":\"zhangsan\"}", JSON.toJSONString(user1));

		JSON.addMixInAnnotations(user1.getClass(), Mixin.class);
		Assert.assertEquals("{\"age\":19,\"userName\":\"zhangsan\",\"sex\":\"male\"}", JSON.toJSONString(user1));

		JSON.removeMixInAnnotations(user1.getClass());
	}

	public void test_2() {
		User user1 = new User("lisi", "male", 20);
		Assert.assertEquals("{\"age\":20,\"sex\":\"male\",\"userName\":\"lisi\"}", JSON.toJSONString(user1));

		JSON.addMixInAnnotations(user1.getClass(), Mixin2.class);
		Assert.assertEquals("{\"userName\":\"lisi\"}", JSON.toJSONString(user1));

		JSON.removeMixInAnnotations(user1.getClass());
	}

	public void test_3() {
		User user1 = new User("wangwu", "male", 31);
		Assert.assertEquals("{\"age\":31,\"sex\":\"male\",\"userName\":\"wangwu\"}", JSON.toJSONString(user1));

		JSON.addMixInAnnotations(user1.getClass(), Mixin3.class);
		Assert.assertEquals("{\"age\":31,\"sex\":\"male\"}", JSON.toJSONString(user1));

		JSON.removeMixInAnnotations(user1.getClass());
	}

	public void test_4() throws Exception {
		JSON.addMixInAnnotations(VO.class, Mixin5.class);
		JSON.addMixInAnnotations(VOBuilder.class, Mixin6.class);

		VO vo = JSON.parseObject("{\"id\":12304,\"name\":\"ljw\"}", VO.class);

		Assert.assertEquals(12304, vo.getId());
		Assert.assertEquals("ljw", vo.getName());

		JSON.removeMixInAnnotations(VO.class);
		JSON.removeMixInAnnotations(VOBuilder.class);
	}

	@JSONType(serialzeFeatures = { SerializerFeature.QuoteFieldNames })
	public class User {
		private String userName;
		private String sex;
		private int age;

		public User(String userName, String sex, int age) {
			this.userName = userName;
			this.sex = sex;
			this.age = age;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

	}

	@JSONType(orders = { "age", "userName", "sex" })
	interface Mixin {
	}

	@JSONType(includes = { "userName" })
	interface Mixin2 {
	}

	@JSONType(ignores = { "userName" })
	interface Mixin3 {
	}

	@JSONType(serialzeFeatures = { SerializerFeature.PrettyFormat })
	interface Mixin4 {
	}

	public static class VO {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	private static class VOBuilder {

		private VO vo = new VO();

		public VO xxx() {
			return vo;
		}

		public VOBuilder withId(int id) {
			vo.id = id;
			return this;
		}

		public VOBuilder withName(String name) {
			vo.name = name;
			return this;
		}
	}

	@JSONType(builder= VOBuilder.class)
	class Mixin5{ }
	@JSONPOJOBuilder(buildMethod="xxx")
	class Mixin6{ }
}
