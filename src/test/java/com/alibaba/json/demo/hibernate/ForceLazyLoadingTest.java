package com.alibaba.json.demo.hibernate;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.demo.hibernate.data.*;
import junit.framework.TestCase;
import org.hibernate.Hibernate;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;
import java.util.Set;

public class ForceLazyLoadingTest extends TestCase {
    EntityManagerFactory emf;

    protected void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("persistenceUnit");
    }

    protected void tearDown() throws Exception {
        if (emf != null) {
            emf.close();
        }
    }

    public void testGetCustomerJson() throws Exception {

        EntityManager em = emf.createEntityManager();

        // false -> no forcing of lazy loading


        Customer customer = em.find(Customer.class, 103);
        assertFalse(Hibernate.isInitialized(customer.getPayments()));
        String json = JSON.toJSONString(customer);
        System.out.println(json);
        // should force loading...
//            Set<Payment> payments = customer.getPayments();
//            /*
//            System.out.println("--- JSON ---");
//            System.out.println(json);
//            System.out.println("--- /JSON ---");
//            */
//
//            assertTrue(Hibernate.isInitialized(payments));
//            // TODO: verify
//            assertNotNull(json);
//
//            Map<?,?> stuff = mapper.readValue(json, Map.class);
//
//            assertTrue(stuff.containsKey("payments"));
//            assertTrue(stuff.containsKey("orders"));
//            assertNull(stuff.get("orderes"));

    }
}
