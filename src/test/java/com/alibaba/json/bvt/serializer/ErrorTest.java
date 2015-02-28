package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

public class ErrorTest extends TestCase {

    public void test_error() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.put(A.class, new ObjectSerializer() {

            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                         throws IOException {
                throw new IOException();
            }
        });
        JSONSerializer ser = new JSONSerializer(config);

        {
            Exception error = null;
            try {
                ser.write(new A());
            } catch (JSONException ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }

        {
            Exception error = null;
            try {
                B b = new B();
                b.setId(new A());
                ser.write(b);
            } catch (JSONException ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }

    public class A {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

    public class B {

        private A id;

        public A getId() {
            return id;
        }

        public void setId(A id) {
            this.id = id;
        }

    }
}
