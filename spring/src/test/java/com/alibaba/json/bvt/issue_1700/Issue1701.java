package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class Issue1701 {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac) //
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) // 设置服务器端返回的字符集为：UTF-8
                .build();
    }


    @RestController
    @RequestMapping()
    public static class BeanController {

        @PostMapping(path = "/download", produces = "application/octet-stream;charset=UTF-8")
        public @ResponseBody
        ResponseEntity<byte[]> download(@RequestBody TestBean testBean) {

            byte[] body = new byte[0];
            InputStream in;
            try {
                in = Issue1701.class.getClassLoader().getResourceAsStream(testBean.getName());
                body = new byte[in.available()];
                in.read(body);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=1.txt");
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);

            return response;
        }
    }


    @ComponentScan(basePackages = "com.alibaba.json.bvt.issue_1700")
    @Configuration
    @EnableWebMvc
    public static class WebMvcConfig extends WebMvcConfigurerAdapter {

        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
            converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            converters.add(0, converter);
        }
    }

    @Test
    public void testBean() throws Exception {
        mockMvc.perform(
                (post("/download").characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\": \"1.txt\"}")
                )).andExpect(status().isOk()).andDo(print());

    }

    static class TestBean {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
