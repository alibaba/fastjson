package com.alibaba.json.test.performance;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;

public class DecoderPerformanceTest extends TestCase {

    final int      COUNT = 1000 * 100;
    private String text;

    protected void setUp() throws Exception {
        text = "{\"old\":true,\"description\":\"神棍\",\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}";

        text = "[{\"S\":321061,\"T\":\"GetAttributeResp\"},{\"ERROR\":null,\"TS\":0,\"VAL\":{\"SqlList\":[{\"BatchSizeMax\":0,\"BatchSizeTotal\":0,\"ConcurrentMax\":1,\"DataSource\":\"jdbc:wrap-jdbc:filters=default,encoding:name=ds-offer:jdbc:mysql://100.10.10.10:8066/xxx\",\"EffectedRowCount\":0,\"ErrorCount\":0,\"ExecuteCount\":5,\"FetchRowCount\":5,\"File\":null,\"ID\":2001,\"LastError\":null,\"LastTime\":1292742908178,\"MaxTimespan\":16,\"MaxTimespanOccurTime\":1292742668191,\"Name\":null,\"RunningCount\":0,\"SQL\":\"SELECT @@SQL_MODE\",\"TotalTime\":83}]}}]";
        // text =
        // "[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]";
        // text =
        // "[\"description\", \"神棍\", \"校长\", \"old\",\"salary\", \"jdbc:wrap-jdbc:filters=default,encoding:name=ds-offer:jdbc:mysql://100.10.10.10:8066/xxx\"]";
        // text =
        // "{\"OLD\":true,\"DESCRIPTION\":\"神棍\",\"name\":\"校长\",\"AGE\":3,\"SALARY\":123456789.0123}";
        text = "{\"badboy\":true,\"description\":\"神棍敌人姐\",\"name\":\"校长\",\"age\":3,\"birthdate\":1293278091773,\"salary\":123456789.0123}";

        String resource = "json/group.json";
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        text = IOUtils.toString(is);
        is.close();
    }

    public void test_performance() throws Exception {

        List<Codec> decoders = new ArrayList<Codec>();
        decoders.add(new JacksonCodec());
        decoders.add(new FastjsonCodec());

        for (int i = 0; i < 20; ++i) {
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
