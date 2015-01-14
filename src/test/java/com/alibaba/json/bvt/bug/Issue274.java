package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Issue274 extends TestCase {

    public void test() throws Exception {
        Customer cus = new Customer();
        cus.setId(1L);
        cus.setName("name");

        Object json = JSON.toJSON(cus);
        System.out.println(json);

        String cusJson = json.toString();

        cusJson = "{\"name\":\"name\",\"id\":1}";

        Customer customer = JSON.parseObject(cusJson, Customer.class);

        System.out.println(customer);
    }

    public interface Indexable<ID extends Serializable> {

        public ID getId();

        public void setId(ID id);
    }

    public static class Customer implements Indexable<Long> {

        private Long   id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Customer [id=" + id + ", name=" + name + "]";
        }

        // remove this to then no longer throw exception
        public Map<String, Object> toIndexMap() {

            return null;
        }

    }
}
