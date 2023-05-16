package com.alibaba.fastjson;

import java.io.InputStreamReader;
import java.io.OutputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;


public class JSONMapper {
  
  public <T> readValue(InputStream src, JavaType Class<T> valueType) throws IOException, Exception {
    InputStream input = new InputStream(src);
  }

  public String writeValueAsString(Object value) throws Exception {
     String data = (String) value.toString();
     return data;
  }
}

