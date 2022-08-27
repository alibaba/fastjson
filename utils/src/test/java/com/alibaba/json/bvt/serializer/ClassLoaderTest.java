package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.json.demo.X;

public class ClassLoaderTest extends TestCase {

    private ClassLoader ctxLoader;

    protected void setUp() throws Exception {
        ctxLoader = Thread.currentThread().getContextClassLoader();
    }

    protected void tearDown() throws Exception {
        Thread.currentThread().setContextClassLoader(ctxLoader);
    }

    public void test_error() throws Exception {
        Field field = ServiceLoader.class.getDeclaredField("loadedUrls");
        field.setAccessible(true);
        Set<String> loadedUrls = (Set<String>) field.get(null);

        Thread.currentThread().setContextClassLoader(new MyClassLoader(new ClassCastException()));
        JSON.toJSONString(new A());
        
        loadedUrls.clear();

        Thread.currentThread().setContextClassLoader(new MyClassLoader(new IOException()));
        JSON.toJSONString(new B());

        loadedUrls.clear();

        Thread.currentThread().setContextClassLoader(new EmptyClassLoader());
        JSON.toJSONString(new C());

        loadedUrls.clear();

        Thread.currentThread().setContextClassLoader(new ErrorClassLoader());
        JSON.toJSONString(new D());

        loadedUrls.clear();

        Thread.currentThread().setContextClassLoader(ctxLoader);
        JSON.toJSONString(new E());
    }

    public static class EmptyClassLoader extends ClassLoader {

        public Enumeration<URL> getResources(String name) throws IOException {
            return new Vector<URL>().elements();
        }
    }

    public static class ErrorClassLoader extends ClassLoader {

        public Class<?> loadClass(String name) throws ClassNotFoundException {
            return Object.class;
        }
    }

    public static class MyClassLoader extends ClassLoader {

        private final Exception error;

        public MyClassLoader(Exception error){
            super();
            this.error = error;
        }

        public Enumeration<URL> getResources(String name) throws IOException {
            if (error instanceof IOException) {
                throw (IOException) error;
            }
            throw (RuntimeException) error;
        }
    }

    public class A {

    }

    public class B {

    }

    public class C {

    }

    public class D {

    }

    public class E {

    }
}
