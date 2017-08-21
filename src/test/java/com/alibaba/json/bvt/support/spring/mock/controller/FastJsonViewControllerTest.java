package com.alibaba.json.bvt.support.spring.mock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.alibaba.json.test.entity.Company;
import com.alibaba.json.test.entity.Department;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FastJsonView注解测试controller
 * Created by yanquanyu on 17-5-31.
 */
@Controller
@RequestMapping("fastjsonview")
public class FastJsonViewControllerTest {

    @RequestMapping("test1")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id","name"})})
    public @ResponseBody Company test1() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        return company;
    }

    @RequestMapping("test2")
    @FastJsonView(
            exclude = {@FastJsonFilter(clazz = Company.class,props ={"id","name"})})
    public @ResponseBody Company test2() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        return company;
    }

    @RequestMapping("test3")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id","name","rootDepartment"}),@FastJsonFilter(clazz = Department.class,props = {"description"})})
    public @ResponseBody Company test3() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        Department department = new Department();
        department.setName("部门1");
        department.setDescription("部门1描述");
        department.setId(1L);
        company.setRootDepartment(department);
        return company;
    }

    @RequestMapping("test4")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id","name","rootDepartment"})},
            exclude = {@FastJsonFilter(clazz = Department.class,props = {"description"})})
    public @ResponseBody Company test4() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        Department department = new Department();
        department.setName("部门1");
        department.setDescription("部门1描述");
        department.setId(1L);
        company.setRootDepartment(department);
        return company;
    }

    @RequestMapping("test5")
    @FastJsonView(
            exclude = {@FastJsonFilter(clazz = Department.class,props = {"description"})})
    public @ResponseBody Company test5() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        Department department = new Department();
        department.setName("部门1");
        department.setDescription("部门1描述");
        department.setId(1L);
        company.setRootDepartment(department);
        return company;
    }

    @RequestMapping("test6")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id"})},
            exclude = {@FastJsonFilter(clazz = Company.class,props = {"name"})})
    public @ResponseBody Company test6() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        return company;
    }
}
