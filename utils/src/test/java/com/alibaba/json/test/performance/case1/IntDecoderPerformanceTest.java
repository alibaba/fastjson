package com.alibaba.json.test.performance.case1;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.GsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;
import com.alibaba.json.test.codec.JsonLibCodec;
import com.alibaba.json.test.codec.SimpleJsonCodec;

public class IntDecoderPerformanceTest extends TestCase {

    private String    text;
    private final int COUNT = 1000 * 100;

    protected void setUp() throws Exception {
        String resource;
        resource = "json/int_100.json";
        resource = "json/object_f_int_1000.json";
        // resource = "json/string_array_10000.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        text = IOUtils.toString(is);
        is.close();

        // text =
        // "{\"badboy\":true,\"description\":\"神棍敌人姐\",\"name\":\"校长\",\"age\":3,\"birthdate\":1293278091773,\"salary\":123456789.0123}";
    }

    public void test_performance() throws Exception {

        JSON.parse("true");

        List<Codec> decoders = new ArrayList<Codec>();
        decoders.add(new FastjsonCodec());
        decoders.add(new JacksonCodec());
        decoders.add(new SimpleJsonCodec());
        decoders.add(new JsonLibCodec());
        decoders.add(new GsonCodec());

        for (int i = 0; i < 4; ++i) {
            for (Codec decoder : decoders) {
                decode(text, decoder);
                // decodeToJavaBean(text, decoder);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println(text);
    }

    private void decode(String text, Codec decoder) throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            decoder.decode(text);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(decoder.getName() + " : \t" + NumberFormat.getInstance().format(nano));
    }

    public static class Person {

        private String     name;
        private int        age;
        private BigDecimal salary;
        private Date       birthdate;
        private boolean    old;
        private String     description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isOld() {
            return old;
        }

        public void setOld(boolean old) {
            this.old = old;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

    }
}
