package com.alibaba.json.bvt.support.spring;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.google.common.base.Objects;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;


public class FastJsonRedisSerializerTest {
    private FastJsonRedisSerializer<User> serializer;

    @Before
    public void setUp() {
        this.serializer = new FastJsonRedisSerializer<User>(User.class);
    }

    @Test
    public void test_1() {
        User user = serializer.deserialize(serializer.serialize(new User(1, "土豆", 25)));
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

    @Test
    public void test_5() {
        User user = new User(1, "土豆", 25);
        byte[] serializedValue = serializer.serialize(user);
        Arrays.sort(serializedValue); // corrupt serialization result
        Assert.assertNull(serializer.deserialize(serializedValue));
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
}
