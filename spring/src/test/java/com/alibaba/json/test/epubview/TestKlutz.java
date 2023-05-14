package com.alibaba.json.test.epubview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class TestKlutz extends TestCase {

    private EpubViewBook book;
    
    ObjectMapper mapper = new ObjectMapper();
    Gson gson = new Gson();
    
    private EpubViewBook book_jackson;
    private EpubViewBook book_fastjson;
    
    private int LOOP_COUNT = 1000 * 1;

    @Override
    protected void setUp() throws Exception {
        InputStreamReader isr = new InputStreamReader(
                                                      Thread.currentThread().getContextClassLoader().getResourceAsStream("epub.json"));
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        String temp;
        while ((temp = reader.readLine()) != null) {
            sb.append(temp);
        }
        String s = sb.toString();

        this.book = JSON.parseObject(s, EpubViewBook.class);
    }

    public void test_0() throws Exception {


        for (int j = 0; j < 5; j++) {
            fastjson();
            //gson();
            jackson();

            System.out.println("=======================");
        }
    }

    private String jackson() throws Exception {
        String s = mapper.writeValueAsString(book);
        Long startTime;
        // Jackson
        startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            book_jackson = mapper.readValue(s, EpubViewBook.class);
            //s = mapper.writeValueAsString(epubViewBook);
        }
        System.out.println("Jackson:" + (System.currentTimeMillis() - startTime) + ", " + s.length());
        System.out.println(s);
        return s;
    }

    private String gson() throws Exception {
        String s = gson.toJson(book);
        
        Long startTime;
        // Gson
        startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            EpubViewBook epubViewBook = gson.fromJson(s, EpubViewBook.class);
            s = gson.toJson(epubViewBook);
        }
        System.out.println("Gson:" + (System.currentTimeMillis() - startTime) + ", " + s.length());
        return s;
    }

    private String fastjson() {
        String s = JSON.toJSONString(book, SerializerFeature.DisableCircularReferenceDetect);
        
        Long startTime;
        // Fastjson
        startTime = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            book_fastjson = JSON.parseObject(s, EpubViewBook.class, Feature.DisableCircularReferenceDetect);
            //s = JSON.toJSONString(epubViewBook, SerializerFeature.DisableCircularReferenceDetect);
        }
        System.out.println("Fastjson:" + (System.currentTimeMillis() - startTime) + ", " + s.length());
        System.out.println(s);
        return s;
    }
}
