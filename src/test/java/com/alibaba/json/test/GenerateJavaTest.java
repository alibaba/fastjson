package com.alibaba.json.test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.entity.Company;
import com.alibaba.json.test.entity.Department;
import com.alibaba.json.test.entity.Employee;
import com.alibaba.json.test.entity.Group;

public class GenerateJavaTest extends TestCase {

    private String     text;
    private AtomicLong idSeed = new AtomicLong();

    protected void setUp() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/page_model_cached.json");
        text = IOUtils.toString(is);
        is.close();
    }

    private Long createId() {
        return idSeed.incrementAndGet();
    }

    public void testGenerate() {
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

        String jsonString = JSON.toJSONString(group, true);
        System.out.println(jsonString);
    }
}
