package com.alibaba.json.test.performance;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import com.alibaba.json.test.codec.Codec;
import com.alibaba.json.test.codec.FastjsonCodec;
import com.alibaba.json.test.codec.JacksonCodec;
import com.alibaba.json.test.entity.Company;
import com.alibaba.json.test.entity.Department;
import com.alibaba.json.test.entity.Employee;
import com.alibaba.json.test.entity.Group;

public class ObjectEncodePerformanceTest extends TestCase {

    private final int  COUNT  = 1000 * 100;
    private AtomicLong idSeed = new AtomicLong();

    private Object     object;

    protected void setUp() throws Exception {
        this.object = createObject();
    }

    public void test_decodeObject() throws Exception {
        List<Codec> decoders = new ArrayList<Codec>();
        decoders.add(new JacksonCodec());
        decoders.add(new FastjsonCodec());
        // decoders.add(new SimpleJsonDecoderImpl());
        // decoders.add(new JsonLibDecoderImpl());

        for (Codec decoder : decoders) {
            for (int i = 0; i < 10; ++i) {
                encode(object, decoder);
            }
            System.out.println();
        }
    }

    public void encode(Object object, Codec decoder) throws Exception {
        long startNano = System.nanoTime();
        for (int i = 0; i < COUNT; ++i) {
            decoder.encode(object);
        }
        long nano = System.nanoTime() - startNano;
        System.out.println(decoder.getName() + " : \t" + NumberFormat.getInstance().format(nano));
    }

    private Object createObject() {
        Group group = new Group();

        group.setName("Alibaba Group");
        group.setDescription("Alibaba Group makes it easy for anyone to buy or sell online anywhere in the world. ");

        {
            Company company = new Company();
            company.setId(createId());
            company.setName("Alibaba.com");
            company.setDescription("Global leader in e-commerce for small businesses");
            company.setStock("1688.HK");
            group.getCompanies().add(company);

            Department root = new Department();
            root.setId(createId());
            root.setName("B2B");
            company.setRootDepartment(root);

            {
                Department cbu = new Department();
                cbu.setId(createId());
                cbu.setName("CBU");

                root.getChildren().add(cbu);

                Employee emp0 = new Employee();
                emp0.setName("校长");
                emp0.setDescription("神棍敌人姐");
                emp0.setAge(3);
                emp0.setSalary(new BigDecimal("123456789.0123"));
                emp0.setBirthdate(new Date());
                emp0.setBadboy(true);

                cbu.getMembers().add(emp0);
            }
            {
                Department icbu = new Department();
                icbu.setId(createId());
                icbu.setName("ICBU");

                root.getChildren().add(icbu);
            }
        }

        {
            Company company = new Company();
            company.setId(createId());
            company.setName("Taobao");
            company.setDescription("China's largest online retail website and one-stop platform for shopping, socializing and information sharing");
            group.getCompanies().add(company);
        }
        {
            Company company = new Company();
            company.setId(createId());
            company.setName("Alipay");
            company.setDescription("China's leading third-party online payment platform");
            group.getCompanies().add(company);
        }
        {
            Company company = new Company();
            company.setId(createId());
            company.setName("Alibaba Cloud Computing");
            company.setDescription("Developer of advanced data-centric cloud computing services");
            group.getCompanies().add(company);
        }
        {
            Company company = new Company();
            company.setId(createId());
            company.setName("China Yahoo");
            company.setDescription("One of China's leading Internet portals");
            group.getCompanies().add(company);
        }

        return group;
    }

    private Long createId() {
        return idSeed.incrementAndGet();
    }
}
