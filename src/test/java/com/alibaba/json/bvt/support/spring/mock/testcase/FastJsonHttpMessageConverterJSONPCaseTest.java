package com.alibaba.json.bvt.support.spring.mock.testcase;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class FastJsonHttpMessageConverterJSONPCaseTest {
    private static final MediaType APPLICATION_JAVASCRIPT = new MediaType("application", "javascript");

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @ComponentScan(basePackages = "com.alibaba.json.bvt.support.spring.mock.controller")
    @EnableWebMvc
    @Configuration
    protected static class Config extends WebMvcConfigurerAdapter {
        @Bean
        public JSONPResponseBodyAdvice jsonpResponseBodyAdvice() {
            return new JSONPResponseBodyAdvice();
        }

        @Bean
        FastJsonViewResponseBodyAdvice fastJsonViewResponseBodyAdvice() {
            return new FastJsonViewResponseBodyAdvice();
        }


        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(0, new FastJsonHttpMessageConverter());
            super.extendMessageConverters(converters);
        }
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac) //
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // 设置服务器端返回的字符集为：UTF-8
                .build();
    }

    @Test
    public void isInjectComponent() {
        wac.getBean(JSONPResponseBodyAdvice.class);
        wac.getBean(FastJsonViewResponseBodyAdvice.class);
    }

    @Test
    public void test1() throws Exception {
        mockMvc.perform(
                (post("/jsonp-fastjsonview/test1").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void test1_2() throws Exception {


        ResultActions actions = mockMvc.perform((post("/jsonp-fastjsonview/test1?callback=fnUpdateSome").characterEncoding(
                "UTF-8").contentType(MediaType.APPLICATION_JSON)));
        actions.andDo(print());
        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/fnUpdateSome({\"id\":100,\"name\":\"测试\"})"));
    }

    @Test
    public void test2() throws Exception {


        mockMvc.perform(
                (post("/jsonp-fastjsonview/test2").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void test2_2() throws Exception {


        ResultActions actions = mockMvc.perform((post("/jsonp-fastjsonview/test2?callback=fnUpdateSome").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)));
        actions.andDo(print());
        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/fnUpdateSome({\"description\":\"fastjsonview注解测试\",\"stock\":\"haha\"})"));
    }

    @Test
    public void test3() throws Exception {
        List<Object> list = this.mockMvc.perform(post("/jsonp-fastjsonview/test3")).andReturn().getResponse()
                .getHeaderValues("Content-Length");
        Assert.assertNotEquals(list.size(), 0);
    }

    @Test
    public void test3_Jsonp_ContentLength() throws Exception{
        ResultActions actions1 = this.mockMvc.perform(post("/jsonp-fastjsonview/test3?callback=func")).andDo(print());
        Object obj1 = actions1.andReturn().getResponse().getHeaderValue("Content-Length");
        Assert.assertNotNull(obj1);
        Assert.assertEquals(85,obj1);

        ResultActions actions2 = this.mockMvc.perform(post("/jsonp-fastjsonview/test3?callback=fnUpdateSome")).andDo(print());
        Object obj2 = actions2.andReturn().getResponse().getHeaderValue("Content-Length");
        Assert.assertNotNull(obj2);
        Assert.assertEquals(93,obj2);
    }

    @Test
    public void test3_2() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jsonp-fastjsonview/test3?callback=fnUpdateSome"));
        actions.andDo(print());
        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))

                .andExpect(content().string("/**/fnUpdateSome({\"id\":100,\"name\":\"测试\",\"rootDepartment\":{\"description\":\"部门1描述\"}})"));
    }

    @Test
    public void test4() throws Exception {


        mockMvc.perform(
                (post("/jsonp-fastjsonview/test4").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))).andDo(print());
    }

    @Test
    public void test4_2() throws Exception {


        ResultActions actions = mockMvc.perform((post("/jsonp-fastjsonview/test4?callback=myUpdate").characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)));
        actions.andDo(print());
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/myUpdate({\"id\":100,\"name\":\"测试\",\"rootDepartment\":{\"id\":1,\"members\":[],\"name\":\"部门1\"}})"));
    }

    @Test
    public void test5() throws Exception {

        String jsonStr = "{\"packet\":{\"smsType\":\"USER_LOGIN\"}}";

        mockMvc.perform(
                (post("/jsonp-fastjsonview/test5").characterEncoding("UTF-8").content(jsonStr)
                        .contentType(MediaType.APPLICATION_JSON))).andDo(print());
    }

    @Test
    public void test5_2() throws Exception {

        String jsonStr = "{\"packet\":{\"smsType\":\"USER_LOGIN\"}}";

        ResultActions actions = mockMvc.perform((post("/jsonp-fastjsonview/test5?callback=myUpdate").characterEncoding("UTF-8")
                .content(jsonStr).contentType(MediaType.APPLICATION_JSON)));
        actions.andDo(print());
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/myUpdate(\"{\\\"packet\\\":{\\\"smsType\\\":\\\"USER_LOGIN\\\"}}\")"));
    }

    @Test
    public void test7() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jsonp-fastjsonview/test7?customizedCallbackParamName=fnUpdateSome"));
        actions.andDo(print());
        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))

                .andExpect(content().string("/**/fnUpdateSome({})"));
    }

    @Test
    public void test8() throws Exception {
        String invalidMethodName = "--methodName";
        ResultActions actions = this.mockMvc.perform(post("/jsonp-fastjsonview/test7?customizedCallbackParamName=" + invalidMethodName));
        actions.andDo(print());
        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/null({})"));
    }

    @Test
    public void test9() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jsonp-fastjsonview/test9?callback=fnUpdateSome"));
        actions.andDo(print());
        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/fnUpdateSome({\"id\":100})"));
    }
}
