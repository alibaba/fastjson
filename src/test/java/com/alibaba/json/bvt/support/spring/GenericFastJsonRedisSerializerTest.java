package com.alibaba.json.bvt.support.spring;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.alibaba.fastjson.util.IOUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Arrays;
import java.util.List;


public class GenericFastJsonRedisSerializerTest {
    private GenericFastJsonRedisSerializer serializer;

    @Before
    public void setUp() {
        this.serializer = new GenericFastJsonRedisSerializer();
    }

    @Test
    public void test_1() {
        User user = (User) serializer.deserialize(serializer.serialize(new User(1, "土豆", 25)));
        Assert.assertTrue(Objects.equal(user.getId(), 1));
        Assert.assertTrue(Objects.equal(user.getName(), "土豆"));
        Assert.assertTrue(Objects.equal(user.getAge(), 25));
    }

    @Test
    public void test_2() {
        Assert.assertThat(serializer.serialize(null), Is.is(new byte[0]));
    }

    @Test
    public void test_3() {
        Assert.assertThat(serializer.deserialize(new byte[0]), IsNull.nullValue());
    }

    @Test
    public void test_4() {
        Assert.assertThat(serializer.deserialize(null), IsNull.nullValue());
    }

    @Test(expected = SerializationException.class)
    public void test_5() {
        User user = new User(1, "土豆", 25);
        byte[] serializedValue = serializer.serialize(user);
        Arrays.sort(serializedValue); // corrupt serialization result
        serializer.deserialize(serializedValue);
    }

    /**
     * for issue #2155
     */
    @Test
    public void test_6() {

        BaseResult<List<String>> baseResult = new BaseResult<List<String>>();
        baseResult.setCode("1000");
        baseResult.setMsg("success");
        baseResult.setData(Lists.newArrayList("测试1", "测试2", "测试3"));

        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        byte[] bytes = genericFastJsonRedisSerializer.serialize(baseResult);
        BaseResult<List<String>> baseResult2 = (BaseResult<List<String>>) genericFastJsonRedisSerializer.deserialize(bytes);

        Assert.assertEquals(baseResult2.getCode(), "1000");
        Assert.assertEquals(baseResult2.getData().size(), 3);

        String json = "{\n" +
                "\"@type\": \"com.alibaba.json.bvt.support.spring.GenericFastJsonRedisSerializerTest$BaseResult\",\n" +
                "\"code\": \"1000\",\n" +
                "\"data\": [\n" +
                "\"按手动控制按钮\",\n" +
                "\"不停机\",\n" +
                "\"不转动\",\n" +
                "\"传动轴振动大\",\n" +
                "\"第一推进器\",\n" +
                "\"电机不运行\",\n" +
                "],\n" +
                "\"msg\": \"success\"\n" +
                "}";

        BaseResult<List<String>> baseResult3 = (BaseResult<List<String>>) genericFastJsonRedisSerializer.deserialize(json.getBytes(IOUtils.UTF8));
        Assert.assertEquals(baseResult3.getCode(), "1000");
        Assert.assertEquals(baseResult3.getData().size(), 6);
    }

    static class User {
        private Integer id;
        private String name;
        private Integer age;

        public User() {
        }

        public User(Integer id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    static class BaseResult<T> {
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        private String msg;
        private String code;
        private T data;
    }
}
