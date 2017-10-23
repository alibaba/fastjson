package com.alibaba.json.bvt.support.spring.mock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.alibaba.fastjson.support.spring.annotation.ResponseJSONP;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonEnumTestVO;
import com.alibaba.json.test.entity.Company;
import com.alibaba.json.test.entity.Department;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;

/**
 * FastJsonView注解测试controller
 * Created by yanquanyu on 17-5-31.
 */
@ResponseJSONP
@Controller
@RequestMapping("jsonp-fastjsonview")
public class FastJsonViewAndJSONPControllerTest {


    @ResponseJSONP
    @RequestMapping("test1")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id","name"})})
    public Company test1() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        return company;
    }


    @ResponseJSONP
    @RequestMapping("test2")
    @FastJsonView(
            exclude = {@FastJsonFilter(clazz = Company.class,props ={"id","name"})})
    public Company test2() {
        Company company = new Company();
        company.setId(100L);
        company.setName("测试");
        company.setDescription("fastjsonview注解测试");
        company.setStock("haha");
        return company;
    }

    @ResponseJSONP
    @RequestMapping("test3")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id","name","rootDepartment"}),@FastJsonFilter(clazz = Department.class,props = {"description"})})
    public Company test3() {
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

    @ResponseJSONP
    @RequestMapping("test4")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = Company.class,props ={"id","name","rootDepartment"})},
            exclude = {@FastJsonFilter(clazz = Department.class,props = {"description", "memebers", "children"})})
    public Company test4() {
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

    @ResponseJSONP
    @RequestMapping("test5")
    public
    @ResponseBody
    String test5(@RequestBody FastJsonEnumTestVO vo) {
        return JSON.toJSONString(vo);
    }

    @ResponseJSONP(callback = "customizedCallbackParamName")
    @RequestMapping("test7")
    public Company test7() {
        Company company = new Company();
        return company;
    }

    @ResponseJSONP
    @RequestMapping("test8")
    public Callable<Company> test8(){
        return new Callable<Company>() {
            public Company call() throws Exception {
                return new Company();
            }
        };
    }


    //使用类上面注解的 @ResponseJSONP
    @RequestMapping("test9")
    public Company test9() {
        Company company = new Company();
        company.setId(100L);
        return company;
    }
}
