package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
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
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by songlingdong on 8/5/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class Issue1405 {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac) //
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // 设置服务器端返回的字符集为：UTF-8
                .build();
    }

    @Controller
    @RequestMapping("fastjson")
    public static class BeanController {

        @RequestMapping(value = "/test1405", method = RequestMethod.GET)
        public
        @ResponseBody
        ModelAndView test7() {

            AuthIdentityRequest authRequest = new AuthIdentityRequest();
            authRequest.setAppId("cert01");
            authRequest.setUserId(2307643);
            authRequest.setIdNumber("34324324234234");
            authRequest.setRealName("杨力");
            authRequest.setBusinessLine("");
            authRequest.setIgnoreIdNumberRepeat(false);
            authRequest.setOffline(false);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", authRequest);
            modelAndView.addObject("title", "testPage");
            modelAndView.setViewName("test");

            return modelAndView;
        }

    }


    @ComponentScan(basePackages = "com.alibaba.json.bvt.issue_1400")
    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE + 1)
    @EnableWebMvc
    public static class WebMvcConfig extends WebMvcConfigurerAdapter {
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
            converters.add(converter);
        }

        @Override
        public void configureViewResolvers(ViewResolverRegistry registry) {
            FastJsonJsonView fastJsonJsonView = new FastJsonJsonView();
            registry.enableContentNegotiation(fastJsonJsonView);
        }
    }

    @Test
    public void test7() throws Exception {

        mockMvc.perform(
                (get("/fastjson/test1405").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )).andExpect(status().isOk()).andDo(print());
    }

    static class AuthIdentityRequest {

        private String appId;
        private int userId;
        private String idNumber;
        private String realName;
        private String businessLine;
        private boolean ignoreIdNumberRepeat;
        private boolean offline;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getBusinessLine() {
            return businessLine;
        }

        public void setBusinessLine(String businessLine) {
            this.businessLine = businessLine;
        }

        public boolean isIgnoreIdNumberRepeat() {
            return ignoreIdNumberRepeat;
        }

        public void setIgnoreIdNumberRepeat(boolean ignoreIdNumberRepeat) {
            this.ignoreIdNumberRepeat = ignoreIdNumberRepeat;
        }

        public boolean isOffline() {
            return offline;
        }

        public void setOffline(boolean offline) {
            this.offline = offline;
        }
    }


}
