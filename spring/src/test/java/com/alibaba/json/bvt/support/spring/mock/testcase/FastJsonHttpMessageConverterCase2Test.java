package com.alibaba.json.bvt.support.spring.mock.testcase;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.alibaba.fastjson.support.spring.JSONPResponseBodyAdvice;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class FastJsonHttpMessageConverterCase2Test {
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
    public void test8() throws Exception {
        mockMvc.perform(
                (post("/jsonp-fastjsonview/test8").characterEncoding("UTF-8")
                        .contentType(FastJsonHttpMessageConverter.APPLICATION_JAVASCRIPT))).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void test8_2() throws Exception {
//        ResultActions actions = mockMvc.perform((post("/jsonp-fastjsonview/test8?callback=fnUpdateSome").characterEncoding(
//                "UTF-8")));
//        actions.andDo(print());
//        actions.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JAVASCRIPT))
//                .andExpect(content().string("fnUpdateSome({\"id\":100,\"name\":\"测试\"})"));

        MvcResult mvcResult = mockMvc.perform(post("/jsonp-fastjsonview/test8?callback=fnUpdateSome").characterEncoding("UTF-8"))
                .andExpect(request().asyncStarted())
                .andReturn();


        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FastJsonHttpMessageConverter.APPLICATION_JAVASCRIPT))
                .andExpect(content().string("/**/fnUpdateSome({})"));
    }


}
