package com.alibaba.json.bvt.support.spring.mock.testcase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.alibaba.fastjson.support.spring.FastJsonpResponseBodyAdvice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FastJsonView注解测试
 * Created by yanquanyu on 17-5-31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:/config/applicationContext-mvc5.xml" })
public class FastJsonViewTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac) //
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // 设置服务器端返回的字符集为：UTF-8
                .build();
    }

    @Test
    public void isInjectComponent() {
        wac.getBean(FastJsonViewResponseBodyAdvice.class);
    }

    /**
     * 只包括简单属性的对象，单独使用include属性
     */
    @Test
    public void test1() throws Exception {
        mockMvc.perform(
                (post("/fastjsonview/test1").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status
                ().isOk()).andDo(print()
        ).andExpect(content().string("{\"id\":100,\"name\":\"测试\"}"));
    }

    /**
     * 只包括简单属性的对象，单独使用exclude属性
     */
    @Test
    public void test2() throws Exception {
        mockMvc.perform(
                (post("/fastjsonview/test2").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status
                ().isOk()).andDo(print()
        ).andExpect(content().string("{\"description\":\"fastjsonview注解测试\",\"stock\":\"haha\"}"));
    }

    /**
     * 复杂对象：包含Department对象的Company对象，两个对象都使用include属性
     */
    @Test
    public void test3() throws Exception {
        mockMvc.perform(
                (post("/fastjsonview/test3").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status
                ().isOk()).andDo(print()).andExpect(content().string("{\"id\":100,\"name\":\"测试\",\"rootDepartment\":{\"description\":\"部门1描述\"}}"));
    }

    /**
     * 复杂对象：包含Department对象的Company对象，两个对象分别使用include和exclude属性
     */
    @Test
    public void test4() throws Exception {
        mockMvc.perform(
                (post("/fastjsonview/test4").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status
                ().isOk()).andDo(print()).andExpect(content().string("{\"id\":100,\"name\":\"测试\",\"rootDepartment\":{\"children\":[],\"id\":1,\"members\":[],\"name\":\"部门1\"}}"));
    }

    /**
     * 复杂对象：包含Department对象的Company对象，Department使用exclude属性
     */
    @Test
    public void test5() throws Exception {
        mockMvc.perform(
                (post("/fastjsonview/test5").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status
                ().isOk()).andDo(print()).andExpect(content().string("{\"description\":\"fastjsonview注解测试\",\"id\":100,\"name\":\"测试\",\"rootDepartment\":{\"children\":[],\"id\":1,\"members\":[],\"name\":\"部门1\"},\"stock\":\"haha\"}"));
    }

    /**
     * 只包括简单属性的对象，同时使用include和exclude属性
     */
    @Test
    public void test6() throws Exception {
        mockMvc.perform(
                (post("/fastjsonview/test6").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status
                ().isOk()).andDo(print()).andExpect(content().string("{\"id\":100}"));
    }
}
