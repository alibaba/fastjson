/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.bvt;

import java.util.Collections;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

public class JSON_toJSONStringTest extends TestCase {

    public void test_0() throws Exception {
        User user = new User();
        user.setId(123);
        user.setName("毛头");

        SerializeConfig mapping = new SerializeConfig();
        mapping.put(User.class, new JavaBeanSerializer(User.class, "id"));

        JSONSerializer serializer = new JSONSerializer(mapping);
        serializer.write(user);
        String jsonString = serializer.toString();

        Assert.assertEquals("{\"id\":123}", jsonString);
    }

    public void test_1() throws Exception {
        User user = new User();
        user.setId(123);
        user.setName("毛头");

        SerializeConfig mapping = new SerializeConfig();
        mapping.put(User.class, new JavaBeanSerializer(User.class, Collections.singletonMap("id", "uid")));

        JSONSerializer serializer = new JSONSerializer(mapping);
        serializer.write(user);
        String jsonString = serializer.toString();

        Assert.assertEquals("{\"uid\":123}", jsonString);
    }

    public static class User {

        private int    id;
        private String name;

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

    }
}
