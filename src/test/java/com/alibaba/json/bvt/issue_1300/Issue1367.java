package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonpHttpMessageConverter4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.Serializable;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by songlingdong on 8/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class Issue1367 {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac) //
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // 设置服务器端返回的字符集为：UTF-8
                .build();
    }





    public static class AbstractController<ID extends Serializable, PO extends GenericEntity<ID>> {

        @PostMapping(path = "/typeVariableBean",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public PO save(@RequestBody PO dto) {
            //do something
            return dto;
        }

    }

    @RestController
    @RequestMapping()
    public static class BeanController extends AbstractController<Long, TypeVariableBean> {



        @PostMapping(path = "/parameterizedTypeBean",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public String parameterizedTypeBean(@RequestBody ParameterizedTypeBean<String> parameterizedTypeBean){
            return parameterizedTypeBean.t;
        }


    }


    @ComponentScan(basePackages = "com.alibaba.json.bvt.issue_1300")
    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE + 1)
    @EnableWebMvc
    public static class WebMvcConfig extends WebMvcConfigurerAdapter {
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
            converters.add(converter);
        }


    }


    @Test
    public void testParameterizedTypeBean() throws Exception {
        mockMvc.perform(
                (post("/parameterizedTypeBean").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"t\": \"neil dong\"}")
                        )).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testTypeVariableBean() throws Exception {
        mockMvc.perform(
                (post("/typeVariableBean").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"id\": 1}")
                        )).andExpect(status().isOk()).andDo(print());

    }





    static abstract class GenericEntity<ID extends Serializable> {
        public abstract ID getId();
    }

    static class TypeVariableBean extends GenericEntity<Long> {
        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    static class ParameterizedTypeBean<T> {
        private T t;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }



    }
}
